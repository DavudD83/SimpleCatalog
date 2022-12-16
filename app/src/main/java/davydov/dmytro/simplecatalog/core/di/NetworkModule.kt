package davydov.dmytro.simplecatalog.core.di

import dagger.Module
import dagger.Provides
import davydov.dmytro.simplecatalog.BuildConfig
import davydov.dmytro.simplecatalog.core.logger.Logger
import davydov.dmytro.simplecatalog.core.network.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val HTTP_TAG = "HTTP"
        private const val TIMEOUT = 30L
        //can be moved out to build config for debug, qa, release
        private const val BASE_URL = "https://marlove.net/e/mock/v1/"
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(logger: Logger): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { logger.d(HTTP_TAG, it) }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}