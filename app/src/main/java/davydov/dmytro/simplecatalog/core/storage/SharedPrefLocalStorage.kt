package davydov.dmytro.simplecatalog.core.storage

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefLocalStorage @Inject constructor(private val sharedPreferences: SharedPreferences) :
    LocalStorage {

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    override fun getInt(key: String, default: Int): Int = sharedPreferences.getInt(key, default)
}