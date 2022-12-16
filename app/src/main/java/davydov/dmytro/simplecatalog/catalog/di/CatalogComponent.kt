package davydov.dmytro.simplecatalog.catalog.di

import dagger.Component
import davydov.dmytro.simplecatalog.catalog.ui.CatalogFragment
import davydov.dmytro.simplecatalog.core.di.BaseProvisions
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [BaseProvisions::class],
    modules = [DataModule::class, NavigationModule::class]
)
interface CatalogComponent {

    fun inject(fragment: CatalogFragment)

    @Component.Factory
    interface Factory {
        fun create(baseProvisions: BaseProvisions): CatalogComponent
    }
}