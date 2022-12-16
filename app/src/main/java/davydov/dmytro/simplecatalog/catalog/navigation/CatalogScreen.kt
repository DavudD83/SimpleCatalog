package davydov.dmytro.simplecatalog.catalog.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import davydov.dmytro.simplecatalog.catalog.ui.CatalogFragment

class CatalogScreen : FragmentScreen {

    override fun createFragment(factory: FragmentFactory): Fragment {
        return CatalogFragment.newInstance()
    }
}