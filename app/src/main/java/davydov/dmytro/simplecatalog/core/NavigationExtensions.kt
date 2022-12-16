package davydov.dmytro.simplecatalog.core

import com.github.terrakok.cicerone.androidx.AppNavigator
import davydov.dmytro.simplecatalog.R
import davydov.dmytro.simplecatalog.base.BaseActivity

fun BaseActivity.navigator(
    containerId: Int = R.id.container
): AppNavigator = AppNavigator(this, containerId, supportFragmentManager)