package davydov.dmytro.simplecatalog.core.storage

interface LocalStorage {
    fun putInt(key: String, value: Int)
    fun getInt(key: String, default: Int = 0): Int
}