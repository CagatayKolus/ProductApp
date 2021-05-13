package com.cagataykolus.productapp.data.remote.api

import com.cagataykolus.productapp.model.DeleteStatus
import com.cagataykolus.productapp.model.Product
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Service to fetch data using endpoint [PRODUCT_API_URL].
 */
interface ProductApiService {
    @GET("product")
    suspend fun getProducts(): Response<List<Product>>

    @POST("product")
    suspend fun addProduct(@Body product: Product): Response<Product>

    @DELETE("product/{productId}")
    suspend fun deleteProduct(@Path("productId") productId: String): Response<DeleteStatus>

    @PUT("product/{productId}")
    suspend fun editProduct(@Path("productId") productId: String, @Body product: Product): Response<Product>

    companion object {
//        const val PRODUCT_API_URL = "http://127.0.0.1:3001/"
        const val PRODUCT_API_URL = "http://192.168.1.113:3001/"
    }
}