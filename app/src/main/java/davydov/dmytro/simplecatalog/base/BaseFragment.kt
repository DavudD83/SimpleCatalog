package davydov.dmytro.simplecatalog.base

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import davydov.dmytro.simplecatalog.core.ViewModelFactory
import javax.inject.Inject

open class BaseFragment<T>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<T>

    protected val viewModel: T by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)
    }

    protected open fun inject() {}
}