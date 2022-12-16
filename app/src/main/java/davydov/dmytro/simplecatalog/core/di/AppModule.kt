package davydov.dmytro.simplecatalog.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import davydov.dmytro.simplecatalog.core.logger.Logger
import davydov.dmytro.simplecatalog.core.logger.LoggerImpl
import davydov.dmytro.simplecatalog.core.ui.ResourcesStringRepositoryImpl
import davydov.dmytro.simplecatalog.core.ui.StringRepository
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideLogger(): Logger = LoggerImpl()

    @Provides
    @Singleton
    fun provideStringRepo(application: Application): StringRepository =
        ResourcesStringRepositoryImpl(application.resources)
}