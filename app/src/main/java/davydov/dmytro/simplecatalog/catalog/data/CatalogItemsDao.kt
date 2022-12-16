package davydov.dmytro.simplecatalog.catalog.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Single

@Dao
interface CatalogItemsDao {
    @Query("SELECT * FROM catalogItems")
    fun getCatalogItems(): PagingSource<Int, LocalCatalogItem>

    @Insert(onConflict = REPLACE)
    fun insertAll(items: List<LocalCatalogItem>)

    @Query("SELECT * FROM catalogItems WHERE id = (SELECT MAX(id) from catalogItems)")
    fun getLastItem(): LocalCatalogItem?

    @Transaction
    fun replaceAll(items: List<LocalCatalogItem>) {
        deleteAll()
        insertAll(items)
    }

    @Query("DELETE FROM catalogItems")
    fun deleteAll()

    @Query("Select COUNT(*) from catalogItems")
    fun getItemsCount(): Single<Int>
}