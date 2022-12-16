package davydov.dmytro.simplecatalog.core.ui

import android.content.res.Resources
import javax.inject.Inject

class ResourcesStringRepositoryImpl @Inject constructor(private val resources: Resources) :
    StringRepository {

    override fun getString(resId: Int, vararg args: Any): String = resources.getString(resId, *args)
}