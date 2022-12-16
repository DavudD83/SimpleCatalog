package davydov.dmytro.simplecatalog.catalog.data

import javax.inject.Inject

class RemoteToLocalItemMapper @Inject constructor() {
    fun map(list: List<RemoteCatalogItem>): List<LocalCatalogItem> =
        list.map {
            LocalCatalogItem(
                catalogItemId = it._id,
                text = it.text,
                url = it.image,
                confidence = it.confidence
            )
        }
}