package davydov.dmytro.simplecatalog.core

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.fragment.app.Fragment

fun Fragment.isLandscapeOrientation(): Boolean {
    val orientation = resources.configuration.orientation
    return orientation == ORIENTATION_LANDSCAPE
}