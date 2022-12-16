package davydov.dmytro.simplecatalog.catalog.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CatalogItem(val id: String, val text: String, val url: String, val confidence: Float) :
    Parcelable