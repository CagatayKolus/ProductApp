package com.cagataykolus.productapp.ui.detail.review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cagataykolus.productapp.databinding.ItemReviewBinding
import com.cagataykolus.productapp.model.Review
import com.cagataykolus.productapp.ui.detail.review.viewholder.ReviewsViewHolder

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Adapter class [RecyclerView.Adapter] for [RecyclerView] which binds [Review] along with [ReviewsViewHolder]
 * @param onItemClicked which will receive callback when item is clicked.
 */
class ReviewsAdapter(
    private val onItemClicked: (Review) -> Unit
) : ListAdapter<Review, ReviewsViewHolder>(DIFF_CALLBACK) {

    private var list = mutableListOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewsViewHolder(
        ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    fun setData(list: MutableList<Review>) {
        this.list = list
        submitList(list)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem.text == newItem.text

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean =
                oldItem == newItem
        }
    }
}
