package davydov.dmytro.simplecatalog.core.di

import android.app.Application
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import davydov.dmytro.simplecatalog.core.logger.Logger
import davydov.dmytro.simplecatalog.core.storage.CatalogDatabase
import davydov.dmytro.simplecatalog.core.storage.LocalStorage
import davydov.dmytro.simplecatalog.core.ui.StringRepository
import retrofit2.Retrofit

interface BaseProvisions {

    fun provideNavHolder(): NavigatorHolder

    fun provideRouter(): Router

    fun provideRetrofit(): Retrofit

    fun provideDatabase(): CatalogDatabase

    fun provideLocalStorage(): LocalStorage

    fun provideApplication(): Application

    fun provideLogger(): Logger

    fun provideStringRepo(): StringRepository
}