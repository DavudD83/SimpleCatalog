package davydov.dmytro.simplecatalog.catalog.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import davydov.dmytro.simplecatalog.catalog.data.CatalogApi
import davydov.dmytro.simplecatalog.catalog.data.CatalogItemsDao
import davydov.dmytro.simplecatalog.catalog.data.CatalogRepositoryImpl
import davydov.dmytro.simplecatalog.catalog.domain.CatalogRepository
import davydov.dmytro.simplecatalog.core.storage.CatalogDatabase
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun repository(impl: CatalogRepositoryImpl): CatalogRepository

    @Module
    companion object {
        @Provides
        @Reusable
        @JvmStatic
        fun provideCatalogApi(retrofit: Retrofit): CatalogApi = retrofit.create()

        @Provides
        @Singleton
        @JvmStatic
        fun provideCatalogDao(database: CatalogDatabase): CatalogItemsDao =
            database.catalogItemsDao()
    }
}