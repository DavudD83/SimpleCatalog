package davydov.dmytro.simplecatalog.catalog.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.paging.rxjava3.cachedIn
import davydov.dmytro.simplecatalog.R
import davydov.dmytro.simplecatalog.base.BaseViewModel
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import davydov.dmytro.simplecatalog.catalog.domain.LoadCatalogItemsUseCase
import davydov.dmytro.simplecatalog.catalog.navigation.CatalogNavigator
import davydov.dmytro.simplecatalog.core.logTag
import davydov.dmytro.simplecatalog.core.logger.Logger
import davydov.dmytro.simplecatalog.core.ui.StringRepository
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CatalogViewModel @Inject constructor(
    private val loadCatalogItemsUseCase: LoadCatalogItemsUseCase,
    private val stringRepository: StringRepository,
    private val catalogNavigator: CatalogNavigator,
    private val logger: Logger
) : BaseViewModel() {

    private val _catalogItems = MutableLiveData<PagingData<CatalogItem>>(PagingData.empty())
    val catalogItems: LiveData<PagingData<CatalogItem>> = _catalogItems

    private val _generalState = MutableLiveData<GeneralCatalogUiState>()
    val generalState: LiveData<GeneralCatalogUiState> = _generalState

    init {
        loadCatalogItems()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadCatalogItems() {
        loadCatalogItemsUseCase()
            .onErrorResumeNext {
                logger.e(
                    logTag,
                    it,
                    "Something went wrong during PagingData loading. This state is not recoverable!"
                )
                _generalState.postValue(
                    GeneralCatalogUiState(
                        initialErrorText = stringRepository.getString(R.string.initial_error_message)
                    )
                )
                Flowable.just(PagingData.empty())
            }
            .cachedIn(viewModelScope)
            .subscribe { _catalogItems.value = it }
            .let(compositeDisposable::add)
    }

    fun onLoadStateChanged(state: CombinedLoadStates, itemCount: Int) {
        val errorText = if (state.refresh is LoadState.Error) {
            stringRepository.getString(R.string.initial_error_message)
        } else {
            null
        }
        val emptyText =
            if (state.refresh is LoadState.NotLoading && state.append.endOfPaginationReached
                && itemCount == 0
            ) {
                stringRepository.getString(R.string.empty_items_message)
            } else {
                null
            }
        val newState = GeneralCatalogUiState(
            initialErrorText = errorText,
            emptyItemsText = emptyText,
            initialLoadingVisible = state.refresh is LoadState.Loading,
            listVisible = state.refresh is LoadState.NotLoading && emptyText.isNullOrEmpty()
        )
        _generalState.value = newState
    }

    fun onItemClicked(item: CatalogItem) {
        catalogNavigator.openDetails(item)
    }
}