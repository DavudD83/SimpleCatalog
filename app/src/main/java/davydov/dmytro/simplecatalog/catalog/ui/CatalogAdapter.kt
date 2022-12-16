package davydov.dmytro.simplecatalog.catalog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import davydov.dmytro.simplecatalog.R
import davydov.dmytro.simplecatalog.catalog.domain.CatalogItem
import davydov.dmytro.simplecatalog.core.loadImage
import davydov.dmytro.simplecatalog.databinding.ItemCatalogBinding

class CatalogAdapter(private val onItemClick: (CatalogItem) -> Unit) :
    PagingDataAdapter<CatalogItem, CatalogAdapter.Holder>(DiffCallback) {

    private object DiffCallback : ItemCallback<CatalogItem>() {
        override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean =
            oldItem == newItem
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemCatalogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    class Holder(
        private val binding: ItemCatalogBinding,
        private val onItemClick: (CatalogItem) -> Unit
    ) : ViewHolder(binding.root) {
        fun bind(item: CatalogItem) {
            binding.run {
                root.setOnClickListener { onItemClick(item) }
                image.loadImage(
                    url = item.url,
                    loadingPlaceholder = R.drawable.image_placeholder,
                    errorPlaceholder = R.drawable.image_placeholder
                )
                id.text = item.id
                text.text = item.text
                confidence.text = item.confidence.toString()
            }
        }
    }
}