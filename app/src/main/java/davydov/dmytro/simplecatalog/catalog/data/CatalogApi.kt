package davydov.dmytro.simplecatalog.catalog.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CatalogApi {

    companion object {
        private const val MAX_ID_QUERY_KEY = "max_id"
        private const val SINCE_ID_QUERY_KEY = "since_id"
    }

    @GET("items")
    fun loadCatalogItems(
        @Query(MAX_ID_QUERY_KEY) maxId: String?,
        @Query(SINCE_ID_QUERY_KEY) sinceId: String?
    ): Single<List<RemoteCatalogItem>>
}