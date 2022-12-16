package davydov.dmytro.simplecatalog.catalog.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class LoadCatalogItemsUseCase @Inject constructor(private val catalogRepository: CatalogRepository) {

    operator fun invoke(): Flowable<PagingData<CatalogItem>> =
        catalogRepository.loadCatalogItems()
}