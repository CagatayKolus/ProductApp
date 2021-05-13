package com.cagataykolus.productapp.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataykolus.productapp.data.repository.ProductRepository
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.State
import com.cagataykolus.productapp.ui.detail.review.ReviewsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Çağatay Kölüş on 12.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * ViewModel for [ReviewsViewModel].
 */
@ExperimentalCoroutinesApi
@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val _productsLiveData = MutableLiveData<State<List<Product>>>()

    val productsLiveData: LiveData<State<List<Product>>> = _productsLiveData

    private val _deletedProductLiveData = MutableLiveData<State<Product>>()

    val deletedProductLiveData: LiveData<State<Product>> = _deletedProductLiveData

    private val _editedProductLiveData = MutableLiveData<State<Product>>()

    val editedProductLiveData: LiveData<State<Product>> = _editedProductLiveData

    private val _addedProductLiveData = MutableLiveData<State<Product>>()

    val addedProductLiveData: LiveData<State<Product>> = _addedProductLiveData

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .onStart { _productsLiveData.value = State.loading() }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _productsLiveData.value = state }
        }
    }

    fun deleteProduct(productId: String){
        viewModelScope.launch {
            productRepository.deleteProduct(productId)
                .onStart { _deletedProductLiveData.value = State.loading() }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _deletedProductLiveData.value = state }
        }
    }

//    fun addProduct(productId: String, productName: String, productDescription: String){
//        viewModelScope.launch {
//            productRepository.addProduct(productId, productName, productDescription)
//                .onStart { _productsLiveData.value = State.loading() }
//                .map { resource -> State.fromResource(resource) }
//                .collect { state -> _productsLiveData.value = state }
//        }
//    }

    fun addProduct(productId: String, productName: String, productDescription: String){
        viewModelScope.launch {
            productRepository.addProduct(productId, productName, productDescription)
                .onStart { _addedProductLiveData.value = State.loading() }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _addedProductLiveData.value = state }
        }
    }

    fun editProduct(productId: String, productName: String, productDescription: String){
        viewModelScope.launch {
            productRepository.editProduct(productId, productName, productDescription)
                .onStart { _editedProductLiveData.value = State.loading() }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _editedProductLiveData.value = state }
        }
    }
}