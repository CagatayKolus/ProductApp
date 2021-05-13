package com.cagataykolus.productapp.ui.detail.review.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.cagataykolus.productapp.databinding.ItemReviewBinding
import com.cagataykolus.productapp.model.Review

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [RecyclerView.ViewHolder] implementation to inflate View for RecyclerView.
 */
class ReviewsViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(review: Review, onItemClicked: (Review) -> Unit) {
        binding.ratingbarReviewRating.rating = review.rating.toFloat()
        binding.textviewReviewContent.text = review.text

        binding.root.setOnClickListener {
            onItemClicked(review)
        }
    }
}