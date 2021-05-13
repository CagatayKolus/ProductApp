package com.cagataykolus.productapp.di.module

import com.cagataykolus.productapp.data.remote.api.ProductApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

@InstallIn(SingletonComponent::class)
@Module
class ProductApiServiceModule {
    @Singleton
    @Provides
    fun provideRetrofitService(): ProductApiService = Retrofit.Builder()
        .baseUrl(ProductApiService.PRODUCT_API_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .build()
        .create(ProductApiService::class.java)
}
