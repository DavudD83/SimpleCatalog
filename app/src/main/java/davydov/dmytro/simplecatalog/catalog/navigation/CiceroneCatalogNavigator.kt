package davydov.dmytro.simplecatalog.catalog.navigation

import com.github.terrakok.cicerone.Router
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import davydov.dmytro.simplecatalog.details.navigation.CatalogItemDetailsScreen
import javax.inject.Inject

class CiceroneCatalogNavigator @Inject constructor(private val router: Router) :
    CatalogNavigator {

    override fun openDetails(item: CatalogItem) {
        router.navigateTo(CatalogItemDetailsScreen(item))
    }
}