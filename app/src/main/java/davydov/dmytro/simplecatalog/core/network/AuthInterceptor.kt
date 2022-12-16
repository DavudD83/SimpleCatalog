package davydov.dmytro.simplecatalog.core.network

import davydov.dmytro.simplecatalog.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val AUTHORIZATION_HEADER_KEY = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithAuthHeader = chain.request().newBuilder()
            .addHeader(AUTHORIZATION_HEADER_KEY, BuildConfig.AUTH_TOKEN)
            .build()
        return chain.proceed(requestWithAuthHeader)
    }
}