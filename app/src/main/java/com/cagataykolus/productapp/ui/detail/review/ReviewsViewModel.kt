package com.cagataykolus.productapp.ui.detail.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagataykolus.productapp.data.repository.ReviewRepository
import com.cagataykolus.productapp.model.Review
import com.cagataykolus.productapp.model.State
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
class ReviewsViewModel @Inject constructor(private val reviewRepository: ReviewRepository) :
    ViewModel() {

    private val _reviewsLiveData = MutableLiveData<State<List<Review>>>()

    val reviewsLiveData: LiveData<State<List<Review>>> = _reviewsLiveData

    private val _newReviewLiveData = MutableLiveData<State<Review>>()

    val newReviewLiveData: LiveData<State<Review>> = _newReviewLiveData


    fun getReviews(productId: String) {
        viewModelScope.launch {
            reviewRepository.getAllReviews(productId)
                .onStart { _reviewsLiveData.value = State.loading() }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _reviewsLiveData.value = state }
        }
    }

    fun postReview(productId: String, review: Review) {
        viewModelScope.launch {
            reviewRepository.postReview(productId, review)
                .onStart { _newReviewLiveData.value = State.loading() }
                .map { resource -> State.fromResource(resource) }
                .collect { state -> _newReviewLiveData.value = state }
        }
    }
}