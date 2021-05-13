package com.cagataykolus.productapp.ui.products

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cagataykolus.productapp.R
import com.cagataykolus.productapp.databinding.FragmentProductsBinding
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import com.cagataykolus.productapp.model.State
import com.cagataykolus.productapp.ui.base.BaseViewBindingFragment
import com.cagataykolus.productapp.ui.detail.DetailFragment
import com.cagataykolus.productapp.ui.detail.dialog.EditProductDialog
import com.cagataykolus.productapp.ui.detail.dialog.EditProductDialogAction
import com.cagataykolus.productapp.ui.products.adapter.ProductsAdapter
import com.cagataykolus.productapp.ui.products.dialog.AddProductDialog
import com.cagataykolus.productapp.ui.products.dialog.AddProductDialogAction
import com.cagataykolus.productapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * [ProductsFragment] is fragment for [Product] data.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProductsFragment : BaseViewBindingFragment<FragmentProductsBinding>() {
    override fun setBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProductsBinding = FragmentProductsBinding.inflate(inflater)

    private val productsViewModel: ProductsViewModel by viewModels()
    private val mAdapter = ProductsAdapter(this::onItemClicked)

    override fun onResume() {
        super.onResume()
        getProducts()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onStart() {
        super.onStart()

        observeProducts()
        handleNetworkChanges()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchviewProductsSearch.setOnQueryTextListener(filterProducts())

        initView()
        observeDeletedProduct()
    }

    private fun showAddProductDialog() {
        val dialog = AddProductDialog(requireContext())
        dialog.onAction { action ->
            when (action) {
                AddProductDialogAction.AddProductClicked -> {
                    if (isConnectedToInternet()) {
                        hideKeyboard()
                        addProduct(
                            productId = dialog.getProduct().id,
                            productName = dialog.getProduct().name,
                            productDescription = dialog.getProduct().description
                        )
                        dialog.dismissDialog()
                        showToast(getString(R.string.product_added))
                        getProducts()
                    } else {
                        showToast(getString(R.string.internet_connectivity_fail))
                    }
                }
            }
        }.showDialog()
    }

    private fun initView() {
        binding.run {
            recyclerviewProductsList.adapter = mAdapter
            recyclerviewProductsList.setHasFixedSize(true)
            swiperefreshlayoutProductsRefresh.setOnRefreshListener { getProducts() }
        }

        productsViewModel.productsLiveData.value?.let { currentState ->
            if (!currentState.isSuccessful()) {
                getProducts()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_product -> {
                showAddProductDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeProducts() {
        productsViewModel.productsLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        mAdapter.setData(state.data.toMutableList())
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

    private fun observeDeletedProduct() {
        val content = arguments?.getBoolean(DetailFragment.PRODUCT_DELETED)
        content?.let { isDeleted ->
            if (isDeleted) {
                showToast(getString(R.string.product_deleted))
                getProducts()
                arguments?.remove(DetailFragment.PRODUCT_DELETED)
            }
        }
    }

    private fun getProducts() = productsViewModel.getProducts()

    private fun addProduct(productId: String, productName: String, productDescription: String) =
        productsViewModel.addProduct(productId, productName, productDescription)


    private fun filterProducts() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            mAdapter.filter.filter(newText)
            return false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.swiperefreshlayoutProductsRefresh.isRefreshing = isLoading
    }

    private fun onItemClicked(product: Product) {
        navigate(
            R.id.action_productsFragment_to_detailFragment,
            bundleOf(DetailFragment.PRODUCT_CONTENT to product)
        )
    }

    /**
     * Observes network changes.
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(requireContext()).observe(this) { isConnected ->
            if (!isConnected) {
                binding.textviewProductsNetworkStatus.text =
                    getString(R.string.internet_connectivity_fail)
                binding.linearlayoutProductsNetworkStatus.apply {
                    show()
                    setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.connectivity_fail
                        )
                    )
                }
            } else {
                if (productsViewModel.productsLiveData.value is State.Error || mAdapter.itemCount == 0) {
                    getProducts()
                }
                binding.textviewProductsNetworkStatus.text =
                    getString(R.string.internet_connectivity_success)
                binding.linearlayoutProductsNetworkStatus.apply {
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

