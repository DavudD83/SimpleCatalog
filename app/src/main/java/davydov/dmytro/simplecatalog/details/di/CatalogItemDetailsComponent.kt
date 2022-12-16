package davydov.dmytro.simplecatalog.details.di

import dagger.Component
import davydov.dmytro.simplecatalog.core.di.BaseProvisions
import davydov.dmytro.simplecatalog.details.CatalogItemDetailsFragment

@Component(dependencies = [BaseProvisions::class])
interface CatalogItemDetailsComponent {
    fun inject(fragment: CatalogItemDetailsFragment)
}