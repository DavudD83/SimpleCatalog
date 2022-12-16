package davydov.dmytro.simplecatalog.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory<T> @Inject constructor(private val provider: Provider<T>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return try {
            provider.get() as T
        } catch (error : ClassCastException) {
            throw ClassCastException("Wrong view model class type in view model factory")
        }
    }
}