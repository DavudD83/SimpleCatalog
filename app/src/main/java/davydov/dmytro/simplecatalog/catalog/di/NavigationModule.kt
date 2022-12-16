package davydov.dmytro.simplecatalog.catalog.di

import dagger.Binds
import dagger.Module
import davydov.dmytro.simplecatalog.catalog.navigation.CatalogNavigator
import davydov.dmytro.simplecatalog.catalog.navigation.CiceroneCatalogNavigator

@Module
interface NavigationModule {
    @Binds
    fun navigator(impl: CiceroneCatalogNavigator): CatalogNavigator
}