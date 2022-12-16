package davydov.dmytro.simplecatalog.catalog.data

import androidx.paging.*
import androidx.paging.rxjava3.flowable
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import davydov.dmytro.simplecatalog.catalog.domain.CatalogRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val remoteMediator: CatalogRemoteMediator,
    private val catalogDao: CatalogItemsDao
) : CatalogRepository {

    companion object {
        private const val PAGE_SIZE = 10
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun loadCatalogItems(): Flowable<PagingData<CatalogItem>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE / 2
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { catalogDao.getCatalogItems() }
    )
        .flowable
        .map { pagingDataLocal ->
            pagingDataLocal.map { it.toDomain() }
        }
}