package davydov.dmytro.simplecatalog.catalog.data

import androidx.paging.*
import androidx.paging.rxjava3.RxRemoteMediator
import davydov.dmytro.simplecatalog.core.storage.LocalStorage
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatalogRemoteMediator @Inject constructor(
    private val catalogApi: CatalogApi,
    private val catalogDao: CatalogItemsDao,
    private val remoteToLocalItemMapper: RemoteToLocalItemMapper,
    private val localStorage: LocalStorage
) : RxRemoteMediator<Int, LocalCatalogItem>() {

    companion object {
        const val MAX_ID = 0
        const val SINCE_ID = 1
        const val NEXT_KEY_STRATEGY = "strategy"
    }

    override fun initializeSingle(): Single<InitializeAction> = catalogDao.getItemsCount()
        .subscribeOn(Schedulers.io())
        .map { count ->
            if (count > 0) {
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, LocalCatalogItem>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .flatMap {
                val lastItemId = when (loadType) {
                    LoadType.REFRESH -> null
                    LoadType.PREPEND -> null
                    LoadType.APPEND -> {
                        val lastItem = catalogDao.getLastItem()
                        lastItem?.catalogItemId
                    }
                }
                when {
                    loadType == LoadType.PREPEND -> Single.just(
                        MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    )
                    loadType == LoadType.APPEND && lastItemId == null -> Single.just(
                        MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    )
                    else -> {
                        val (maxId, sinceId) = getNextKeys(lastItemId)
                        catalogApi.loadCatalogItems(maxId = maxId, sinceId = sinceId)
                            .flatMap { remoteItems ->
                                val localItems = remoteToLocalItemMapper.map(remoteItems)
                                if (loadType == LoadType.REFRESH) {
                                    saveNextKeyStrategyFor(localItems)
                                    catalogDao.replaceAll(localItems)
                                } else {
                                    catalogDao.insertAll(localItems)
                                }
                                val endReached = localItems.isEmpty()
                                Single.just<MediatorResult>(
                                    MediatorResult.Success(endOfPaginationReached = endReached)
                                )
                            }
                            .onErrorResumeNext { Single.just(MediatorResult.Error(it)) }
                    }
                }
            }
    }

    private fun saveNextKeyStrategyFor(localItems: List<LocalCatalogItem>) {
        if (localItems.size > 1) {
            val strategy = if (localItems.first().catalogItemId > localItems.last().catalogItemId) {
                MAX_ID
            } else {
                SINCE_ID
            }
            localStorage.putInt(NEXT_KEY_STRATEGY, strategy)
        }
    }

    //(maxId, sinceId)
    private fun getNextKeys(lastItemId: String?): Pair<String?, String?> {
        return when (localStorage.getInt(NEXT_KEY_STRATEGY, MAX_ID)) {
            MAX_ID -> lastItemId to null
            SINCE_ID -> null to lastItemId
            else -> throw IllegalArgumentException("Unsupported next key strategy")
        }
    }
}