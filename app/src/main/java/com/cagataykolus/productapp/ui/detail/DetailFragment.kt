package com.cagataykolus.productapp.ui.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.cagataykolus.productapp.R
import com.cagataykolus.productapp.databinding.FragmentDetailBinding
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import com.cagataykolus.productapp.model.State
import com.cagataykolus.productapp.ui.base.BaseViewBindingFragment
import com.cagataykolus.productapp.ui.detail.dialog.EditProductDialog
import com.cagataykolus.productapp.ui.detail.dialog.EditProductDialogAction
import com.cagataykolus.productapp.ui.detail.dialog.ReviewDialog
import com.cagataykolus.productapp.ui.detail.dialog.ReviewDialogAction
import com.cagataykolus.productapp.ui.detail.review.ReviewsViewModel
import com.cagataykolus.productapp.ui.detail.review.adapter.ReviewsAdapter
import com.cagataykolus.productapp.ui.products.ProductsViewModel
import com.cagataykolus.productapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [DetailFragment] is fragment for [Review] data.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : BaseViewBindingFragment<FragmentDetailBinding>() {
    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding = FragmentDetailBinding.inflate(inflater)

    private val reviewsViewModel: ReviewsViewModel by viewModels()
    private val productsViewModel: ProductsViewModel by viewModels()
    private val mAdapter = ReviewsAdapter(this::onItemClicked)

    private lateinit var currentProductId: String

    companion object {
        const val PRODUCT_CONTENT = "PRODUCT"
        const val PRODUCT_DELETED = "DELETED"
    }

    override fun onStart() {
        super.onStart()

        observeReviews()
        observeDeleteProduct()
        handleNetworkChanges()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getParcelable<Product>(PRODUCT_CONTENT)
        content?.let {
            initView(it)
        }
    }

    private fun initView(product: Product) {
        binding.run {
            // Fill details
            currentProductId = product.id
            textviewDetailProduct.text = product.name
            textviewDetailDescription.text = product.description
            textviewDetailCurrency.text = product.currency
            textviewDetailPrice.text = product.price.toString()

            // Get reviews
            recyclerviewDetailReviews.adapter = mAdapter
            recyclerviewDetailReviews.setHasFixedSize(true)
            swiperefreshlayoutDetailRefresh.setOnRefreshListener { getReviews(currentProductId) }
            floatingactionbuttonDetailReview.setOnClickListener {
                showReviewDialog(currentProductId)
            }

            // Delete product
            buttonDetailDeleteProduct.setOnClickListener {
                showDeleteProductDialog()
            }

            // Edit product
            buttonDetailEditProduct.setOnClickListener {
                showEditProductDialog(currentProductId)
            }
        }

        reviewsViewModel.reviewsLiveData.value?.let { currentState ->
            if (!currentState.isSuccessful()) {
                getReviews(product.id)
            }
        }
    }

    private fun showReviewDialog(productId: String) {
        val dialog = ReviewDialog(requireContext(), productId)
        dialog.onAction { action ->
            when (action) {
                ReviewDialogAction.SendReviewClicked -> {
                    if (isConnectedToInternet()) {
                        postReview(productId = currentProductId, review = dialog.getReview())
                        dialog.dismissDialog()
                        getReviews(productId = currentProductId)
                    } else {
                        showToast(getString(R.string.internet_connectivity_fail))
                    }
                }
            }
        }.showDialog()
    }

    private fun showEditProductDialog(productId: String) {
        val dialog = EditProductDialog(requireContext(), productId)
        dialog.onAction { action ->
            when (action) {
                EditProductDialogAction.EditProductClicked -> {
                    if (isConnectedToInternet()) {
                        editProduct(
                            productId = dialog.getProduct().id,
                            productName = dialog.getProduct().name,
                            productDescription = dialog.getProduct().description
                        )
                        dialog.dismissDialog()

                        // Update UI
                        binding.textviewDetailProduct.text = dialog.getProduct().name
                        binding.textviewDetailDescription.text = dialog.getProduct().description
                    } else {
                        showToast(getString(R.string.internet_connectivity_fail))
                    }
                }
            }
        }.showDialog()
    }

    private fun showDeleteProductDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.delete_product_title))
        alertDialog.setMessage(getString(R.string.delete_product_description))
        alertDialog.setPositiveButton(
            getString(R.string.yes)
        ) { _, _ ->
            if (isConnectedToInternet()) {
                deleteProduct(currentProductId)
            } else {
                showToast(getString(R.string.internet_connectivity_fail))
            }
        }
        alertDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }

    private fun observeReviews() {
        reviewsViewModel.reviewsLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    state.data.forEach {
                        Log.d("cgty", "productId: " + it.productId + " text: " + it.text)
                    }
                    if (state.data.isNotEmpty()) {
                        val filteredList = mutableListOf<Review>()
                        state.data.forEach {
                            if (it.productId == currentProductId) {
                                filteredList.add(it)
                            }
                        }
                        mAdapter.setData(filteredList)
                    }
                    showLoading(false)
                }
                is State.Error -> {
                    showToast(state.message)
                    showLoading(false)
                }
            }
        }
    }

    private fun observeDeleteProduct() {
        productsViewModel.deletedProductLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> showProductLoading(true)
                is State.Success -> {
                    navigate(
                        R.id.action_detailFragment_to_productsFragment,
                        bundleOf(PRODUCT_DELETED to true)
                    )
                    showProductLoading(false)
                }
                is State.Error -> {
                    showToast(state.message)
                    showProductLoading(false)
                }
            }
        }
    }


    private fun getReviews(productId: String) = reviewsViewModel.getReviews(productId)

    private fun postReview(productId: String, review: Review) =
        reviewsViewModel.postReview(productId, review)

    private fun deleteProduct(productId: String) = productsViewModel.deleteProduct(productId)


    private fun editProduct(productId: String, productName: String, productDescription: String) =
        productsViewModel.editProduct(productId, productName, productDescription)


    private fun showLoading(isLoading: Boolean) {
        binding.swiperefreshlayoutDetailRefresh.isRefreshing = isLoading
    }

    private fun showProductLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbarDetailLoading.visibility = View.VISIBLE
        } else {
            binding.progressbarDetailLoading.visibility = View.GONE
        }
    }

    private fun onItemClicked(review: Review) {
        // Clicked item.
    }

    /**
     * Observes network changes.
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(requireContext()).observe(this) { isConnected ->
            if (!isConnected) {
                binding.textviewDetailNetworkStatus.text =
                    getString(R.string.internet_connectivity_fail)

                getReviews(currentProductId)

                binding.linearlayoutDetailNetworkStatus.apply {
                    show()
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.connectivity_fail
                        )
                    )
                }
            } else {
                if (reviewsViewModel.reviewsLiveData.value is State.Error || mAdapter.itemCount == 0) {
                    getReviews(currentProductId)
                }
                binding.textviewDetailNetworkStatus.text =
                    getString(R.string.internet_connectivity_success)
                binding.linearlayoutDetailNetworkStatus.apply {
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.connectivity_success
                        )
                    )

                    animate()
                        .alpha(1f)
                        .setStartDelay(1000L)
                        .setDuration(1000L)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        }
    }

}