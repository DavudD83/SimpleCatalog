package davydov.dmytro.simplecatalog.catalog.ui

data class GeneralCatalogUiState(
    private val initialErrorText: String? = null,
    private val emptyItemsText: String? = null,
    val initialLoadingVisible: Boolean = false,
    val listVisible: Boolean = false
) {
    val initialMessageVisible: Boolean
        get() = initialErrorText != null || emptyItemsText != null

    val initialMessage: String?
        get() = initialErrorText ?: emptyItemsText
}