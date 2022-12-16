package davydov.dmytro.simplecatalog.catalog.navigation

import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem

interface CatalogNavigator {
    fun openDetails(item: CatalogItem)
}