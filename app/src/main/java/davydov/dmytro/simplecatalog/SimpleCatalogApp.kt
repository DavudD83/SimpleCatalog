package davydov.dmytro.simplecatalog

import davydov.dmytro.simplecatalog.base.BaseApplication
import davydov.dmytro.simplecatalog.core.di.AppComponent
import davydov.dmytro.simplecatalog.core.di.AppModule
import davydov.dmytro.simplecatalog.core.di.DaggerAppComponent

class SimpleCatalogApp : BaseApplication() {

    override lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .factory()
            .create(AppModule(this))
            .also { appComponent = it }
            .inject(this)
    }
}