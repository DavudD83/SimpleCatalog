package davydov.dmytro.simplecatalog.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import davydov.dmytro.simplecatalog.catalog.data.CatalogItemsDao
import davydov.dmytro.simplecatalog.catalog.data.LocalCatalogItem

@Database(entities = [LocalCatalogItem::class], version = 1)
abstract class CatalogDatabase : RoomDatabase() {

    abstract fun catalogItemsDao(): CatalogItemsDao
}