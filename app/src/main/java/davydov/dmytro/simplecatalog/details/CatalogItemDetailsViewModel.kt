package davydov.dmytro.simplecatalog.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import davydov.dmytro.simplecatalog.base.BaseViewModel
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import javax.inject.Inject

class CatalogItemDetailsViewModel @Inject constructor() : BaseViewModel() {

    private val _item = MutableLiveData<CatalogItem>()
    val item: LiveData<CatalogItem> = _item

    fun setup(item: CatalogItem) {
        _item.value = item
    }
}