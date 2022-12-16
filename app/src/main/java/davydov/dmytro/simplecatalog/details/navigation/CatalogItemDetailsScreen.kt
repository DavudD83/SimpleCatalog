package davydov.dmytro.simplecatalog.details.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import davydov.dmytro.simplecatalog.details.CatalogItemDetailsFragment

class CatalogItemDetailsScreen(private val item: CatalogItem) : FragmentScreen {

    override fun createFragment(factory: FragmentFactory): Fragment =
        CatalogItemDetailsFragment.newInstance(item)
}