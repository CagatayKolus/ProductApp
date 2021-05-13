package com.cagataykolus.productapp.data.repository

import androidx.annotation.MainThread
import com.cagataykolus.productapp.data.local.dao.ReviewDao
import com.cagataykolus.productapp.data.remote.api.ReviewApiService
import com.cagataykolus.productapp.model.Review
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

interface ReviewRepository {
    fun getAllReviews(productId: String): Flow<Resource<List<Review>>>
    fun postReview(productId: String, review: Review): Flow<Resource<Review>>
    fun getReviewByName(reviewName: String): Flow<Review>
}

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability. This is single source of data.
 */
@ExperimentalCoroutinesApi
class DefaultReviewRepository @Inject constructor(
    private val reviewDao: ReviewDao,
    private val service: ReviewApiService
) : ReviewRepository {
    /**
     * Fetched the data from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override fun getAllReviews(productId: String): Flow<Resource<List<Review>>> {
        return object : NetworkBoundRepository<List<Review>, List<Review>>() {

            override suspend fun saveRemoteData(response: List<Review>) {
                reviewDao.addReviews(response)
            }

            override fun fetchFromLocal(): Flow<List<Review>> = reviewDao.getAllReviews()

            override suspend fun fetchFromRemote(): Response<List<Review>> = service.getReviews(productId)
        }.asFlow()
    }

    override fun postReview(productId: String, review: Review): Flow<Resource<Review>> {
        return object : NetworkBoundRepository<Review, Review>() {
            override suspend fun saveRemoteData(response: Review) {
                reviewDao.addReview(response)
            }

            override fun fetchFromLocal(): Flow<Review> = reviewDao.getReviewByText(review.text)

            override suspend fun fetchFromRemote(): Response<Review> = service.postReview(productId, review)

        }.asFlow()
    }

    @MainThread
    override fun getReviewByName(reviewName: String): Flow<Review> =
        reviewDao.getReviewByName(reviewName).distinctUntilChanged()
}