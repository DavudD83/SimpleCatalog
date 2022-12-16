package davydov.dmytro.simplecatalog

import android.os.Bundle
import com.github.terrakok.cicerone.Replace
import davydov.dmytro.simplecatalog.base.BaseActivity
import davydov.dmytro.simplecatalog.catalog.navigation.CatalogScreen

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState ?: navigator.applyCommands(arrayOf(Replace(CatalogScreen())))
    }
}