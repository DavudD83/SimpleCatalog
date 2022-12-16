package davydov.dmytro.simplecatalog.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import davydov.dmytro.simplecatalog.core.storage.CatalogDatabase
import davydov.dmytro.simplecatalog.core.storage.LocalStorage
import davydov.dmytro.simplecatalog.core.storage.SharedPrefLocalStorage
import javax.inject.Singleton

@Module
interface StorageModule {

    @Binds
    @Singleton
    fun localStorage(impl: SharedPrefLocalStorage): LocalStorage

    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideDatabase(appContext: Application): CatalogDatabase =
            Room.databaseBuilder(
                appContext.applicationContext,
                CatalogDatabase::class.java,
                "CatalogDatabase.db"
            )
                .build()

        @Provides
        @Singleton
        @JvmStatic
        fun provideSharedPref(appContext: Application): SharedPreferences =
            appContext.getSharedPreferences("local", Context.MODE_PRIVATE)
    }
}