package davydov.dmytro.simplecatalog.core.ui

import androidx.annotation.StringRes

interface StringRepository {
    fun getString(@StringRes resId: Int, vararg args: Any): String
}