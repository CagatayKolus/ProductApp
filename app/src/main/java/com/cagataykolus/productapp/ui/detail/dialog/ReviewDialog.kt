package com.cagataykolus.productapp.ui.detail.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.cagataykolus.productapp.R
import com.cagataykolus.productapp.databinding.DialogReviewBinding
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [ReviewDialog] is dialog for [Review] data.
 */
class ReviewDialog(context: Context, productId: String) : BottomSheetDialog(context) {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context)
    private var action: ((ReviewDialogAction) -> Unit)? = null

    private lateinit var review: Review

    init {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DialogReviewBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.buttonDialogReviewSendReview.setOnClickListener {


            if(isInputCorrect(binding)){
                review = Review(
                    productId = productId,
                    locale = "en",
                    rating = binding.ratingbarDialogReviewRating.rating.toInt(),
                    text = binding.textinputlayoutDialogContainer.editText?.text.toString()
                )

                action?.invoke(ReviewDialogAction.SendReviewClicked)
            }
        }
    }

    private fun isInputCorrect(binding: DialogReviewBinding): Boolean {
        val isSuccessful = true

        // Check TextInputLayout
        if (binding.textinputlayoutDialogContainer.editText?.text?.isEmpty()!!) {
            binding.textinputlayoutDialogContainer.isErrorEnabled = true
            binding.textinputlayoutDialogContainer.error = context.getString(R.string.empty_field)
            return false

        } else {
            binding.textinputlayoutDialogContainer.error = null
            binding.textinputlayoutDialogContainer.isErrorEnabled = false
        }

        // Check RatingBar
        if(binding.ratingbarDialogReviewRating.rating.toInt() == 0 ){
            Toast.makeText(context, context.getString(R.string.empty_rating), Toast.LENGTH_SHORT).show()
            return false
        }

        return isSuccessful
    }

    fun onAction(action: (ReviewDialogAction) -> Unit) = apply {
        this.action = action
    }

    fun showDialog() = apply {
        dialog.show()
    }

    fun dismissDialog() = apply {
        dialog.dismiss()
    }

    fun getReview(): Review {
        return review
    }
}

sealed class ReviewDialogAction {
    object SendReviewClicked : ReviewDialogAction()
}