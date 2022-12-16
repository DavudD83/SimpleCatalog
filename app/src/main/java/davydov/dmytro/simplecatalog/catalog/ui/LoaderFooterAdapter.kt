package davydov.dmytro.simplecatalog.catalog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import davydov.dmytro.simplecatalog.R
import davydov.dmytro.simplecatalog.databinding.ItemCatalogLoadingFooterBinding

class LoaderFooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderFooterAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder = Holder(
        ItemCatalogLoadingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ).also { it.binding.retry.setOnClickListener { retry() } }

    class Holder(val binding: ItemCatalogLoadingFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.errorText.text =
                binding.root.resources.getString(R.string.new_page_loading_error_message)
        }

        fun bind(loadState: LoadState) {
            binding.run {
                val errorVisible = loadState is LoadState.Error
                loader.isInvisible = loadState !is LoadState.Loading
                errorText.isInvisible = !errorVisible
                retry.isInvisible = !errorVisible
            }
        }
    }
}