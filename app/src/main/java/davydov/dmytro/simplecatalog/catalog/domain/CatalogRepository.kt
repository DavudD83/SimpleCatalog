package davydov.dmytro.simplecatalog.catalog.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable

interface CatalogRepository {
    fun loadCatalogItems(): Flowable<PagingData<CatalogItem>>
}