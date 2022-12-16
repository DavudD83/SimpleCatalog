package davydov.dmytro.simplecatalog.catalog.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.google.android.material.transition.MaterialSharedAxis
import davydov.dmytro.simplecatalog.R
import davydov.dmytro.simplecatalog.base.BaseApplication
import davydov.dmytro.simplecatalog.base.BaseFragment
import davydov.dmytro.simplecatalog.catalog.di.DaggerCatalogComponent
import davydov.dmytro.simplecatalog.core.isLandscapeOrientation
import davydov.dmytro.simplecatalog.databinding.FragmentCatalogBinding

class CatalogFragment : BaseFragment<CatalogViewModel>(R.layout.fragment_catalog) {

    companion object {
        private const val SPAN_COUNT = 2
        private const val SINGLE_SPAN = 1

        fun newInstance() = CatalogFragment()
    }

    override fun inject() {
        val application = requireActivity().application as BaseApplication
        DaggerCatalogComponent
            .factory()
            .create(application.appComponent)
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reenterTransition =
            MaterialSharedAxis(MaterialSharedAxis.Z, false)
        exitTransition =
            MaterialSharedAxis(MaterialSharedAxis.Z, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCatalogBinding.bind(view)
        val adapter = CatalogAdapter { viewModel.onItemClicked(it) }
        val footerAdapter = LoaderFooterAdapter { adapter.retry() }
        adapter.addLoadStateListener {
            //ignore first append loading triggered by paging until first page is shown
            if (it.append !is LoadState.NotLoading && adapter.itemCount == 0) {
                return@addLoadStateListener
            }
            footerAdapter.loadState = it.append
            viewModel.onLoadStateChanged(it, adapter.itemCount)
        }
        binding.setupList(footerAdapter)
        binding.list.adapter = ConcatAdapter(adapter, footerAdapter)
        binding.swipeRefreshLayout.setOnRefreshListener { adapter.refresh() }
        viewModel.catalogItems.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        viewModel.generalState.observe(viewLifecycleOwner) {
            binding.list.isVisible = it.listVisible
            binding.initialMessage.text = it.initialMessage
            binding.initialMessage.isVisible = it.initialMessageVisible
            binding.swipeRefreshLayout.isRefreshing = it.initialLoadingVisible
        }
    }

    private fun FragmentCatalogBinding.setupList(footerAdapter: LoaderFooterAdapter) {
        val isLandscape = isLandscapeOrientation()
        if (isLandscape) {
            list.addItemDecoration(
                CatalogLandscapeDividerDecoration(
                    requireContext(),
                    RecyclerView.VERTICAL
                )
            )
            list.addItemDecoration(
                CatalogLandscapeDividerDecoration(
                    requireContext(),
                    RecyclerView.HORIZONTAL
                )
            )
        } else {
            list.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
        val layoutManager = if (isLandscape) {
            GridLayoutManager(requireContext(), SPAN_COUNT).apply {
                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        getSpanSizeFor(position, footerAdapter)
                }
            }
        } else {
            LinearLayoutManager(requireContext())
        }
        list.layoutManager = layoutManager
    }

    private fun FragmentCatalogBinding.getSpanSizeFor(
        adapterPosition: Int,
        footerAdapter: LoaderFooterAdapter
    ): Int {
        val lastItem = adapterPosition == (list.adapter?.itemCount ?: 0) - 1
        val loaderFooterPresent = footerAdapter.itemCount != 0
        return if (lastItem && loaderFooterPresent) {
            SPAN_COUNT
        } else {
            SINGLE_SPAN
        }
    }
}