package davydov.dmytro.simplecatalog.base

import android.app.Application
import davydov.dmytro.simplecatalog.core.di.AppComponent

abstract class BaseApplication : Application() {

    abstract val appComponent: AppComponent
}