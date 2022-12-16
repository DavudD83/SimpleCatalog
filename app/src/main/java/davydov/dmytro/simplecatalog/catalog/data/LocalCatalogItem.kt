package davydov.dmytro.simplecatalog.catalog.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem

@Entity(tableName = "catalogItems")
data class LocalCatalogItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val catalogItemId: String,
    val text: String,
    val url: String,
    val confidence: Float
) {
    fun toDomain(): CatalogItem {
        return CatalogItem(id = catalogItemId, text = text, url = url, confidence = confidence)
    }
}