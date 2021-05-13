package com.cagataykolus.productapp.ui.products.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cagataykolus.productapp.R
import com.cagataykolus.productapp.databinding.ItemProductBinding
import com.cagataykolus.productapp.model.Product

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [RecyclerView.ViewHolder] implementation to inflate View for RecyclerView.
 */
class ProductsViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, onItemClicked: (Product) -> Unit) {
        binding.textviewItemProductId.text = product.id
        binding.textviewItemProductProduct.text = product.name
        binding.textviewItemProductDescription.text = product.description
        binding.textviewItemProductCurrency.text = product.currency
        binding.textviewItemProductPrice.text = product.price.toString()

        binding.root.setOnClickListener {
            onItemClicked(product)
        }
    }
}
