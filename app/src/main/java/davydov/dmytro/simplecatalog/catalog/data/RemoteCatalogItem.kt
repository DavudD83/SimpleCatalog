package davydov.dmytro.simplecatalog.catalog.data

data class RemoteCatalogItem(
    val text: String,
    val confidence: Float,
    val image: String,
    val _id: String
)