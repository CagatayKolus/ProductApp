package com.cagataykolus.productapp.data.remote.api

import com.cagataykolus.productapp.model.Review
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Service to fetch data using endpoint [REVIEW_API_URL].
 */
interface ReviewApiService {
    @GET("reviews/{productId}")
    suspend fun getReviews(@Path("productId") productId: String): Response<List<Review>>

    @POST("reviews/{productId}")
    suspend fun postReview(@Path("productId") productId: String, @Body review: Review): Response<Review>

    companion object {
        //        const val PRODUCT_API_URL = "http://127.0.0.1:3002/"
        const val REVIEW_API_URL = "http://192.168.1.113:3002/"
    }
}