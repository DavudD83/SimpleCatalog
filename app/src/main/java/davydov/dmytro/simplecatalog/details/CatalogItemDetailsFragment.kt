package davydov.dmytro.simplecatalog.details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.transition.MaterialSharedAxis
import davydov.dmytro.simplecatalog.R
import davydov.dmytro.simplecatalog.base.BaseApplication
import davydov.dmytro.simplecatalog.base.BaseFragment
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import davydov.dmytro.simplecatalog.core.loadImage
import davydov.dmytro.simplecatalog.databinding.FragmentCatalogItemDetailsBinding
import davydov.dmytro.simplecatalog.details.di.DaggerCatalogItemDetailsComponent

class CatalogItemDetailsFragment :
    BaseFragment<CatalogItemDetailsViewModel>(R.layout.fragment_catalog_item_details) {

    companion object {
        private const val KEY_ITEM = "ITEM"

        fun newInstance(item: CatalogItem): CatalogItemDetailsFragment =
            CatalogItemDetailsFragment().apply {
                arguments = bundleOf(KEY_ITEM to item)
            }
    }

    override fun inject() {
        val application = requireActivity().application as BaseApplication
        DaggerCatalogItemDetailsComponent
            .builder()
            .baseProvisions(application.appComponent)
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        val item: CatalogItem = requireArguments().getParcelable(KEY_ITEM)!!
        viewModel.setup(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCatalogItemDetailsBinding.bind(view)
        (requireActivity() as AppCompatActivity).run {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        viewModel.item.observe(viewLifecycleOwner) {
            binding.image.loadImage(it.url)
            binding.id.text = it.id
            binding.confidence.text = it.confidence.toString()
            binding.text.text = it.text
        }
    }
}