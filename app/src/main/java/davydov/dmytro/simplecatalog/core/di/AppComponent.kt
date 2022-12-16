package davydov.dmytro.simplecatalog.core.di

import dagger.Component
import davydov.dmytro.simplecatalog.base.BaseActivity
import davydov.dmytro.simplecatalog.base.BaseApplication
import javax.inject.Singleton

@Component(modules = [AppNavigationModule::class, NetworkModule::class, StorageModule::class, AppModule::class])
@Singleton
interface AppComponent : BaseProvisions {

    fun inject(application: BaseApplication)
    fun inject(baseActivity: BaseActivity)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }
}