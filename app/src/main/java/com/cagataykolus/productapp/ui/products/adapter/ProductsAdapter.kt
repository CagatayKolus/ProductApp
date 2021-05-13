package com.cagataykolus.productapp.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cagataykolus.productapp.databinding.ItemProductBinding
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import com.cagataykolus.productapp.ui.detail.review.viewholder.ReviewsViewHolder
import com.cagataykolus.productapp.ui.products.viewholder.ProductsViewHolder
import java.util.*

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Adapter class [RecyclerView.Adapter] for [RecyclerView] which binds [Product] along with [ProductsViewHolder]
 * @param onItemClicked which will receive callback when item is clicked.
 */
class ProductsAdapter(
    private val onItemClicked: (Product) -> Unit
) : ListAdapter<Product, ProductsViewHolder>(DIFF_CALLBACK), Filterable {

    private var list = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductsViewHolder(
        ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    fun setData(list: MutableList<Product>) {
        this.list = list
        submitList(list)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList = mutableListOf<Product>()
                val charSearch = constraint.toString()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(list)
                } else {
                    for (item in list) {
                        if (item.name.toLowerCase().contains(charSearch.toLowerCase(Locale.ROOT)) ||
                            item.description.toLowerCase()
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            filteredList.add(item)
                        }
//                        if (item.price.toString().toLowerCase()
//                                .contains(charSearch.toLowerCase(Locale.ROOT)) ||
//                            item.id.toLowerCase()
//                                .contains(charSearch.toLowerCase(Locale.ROOT))
//                        ) {
//                            filteredList.add(item)
//                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as MutableList<Product>)
            }
        }
    }
}
