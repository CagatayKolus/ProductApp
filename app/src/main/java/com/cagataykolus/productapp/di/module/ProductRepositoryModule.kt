package com.cagataykolus.productapp.di.module

import com.cagataykolus.productapp.data.repository.DefaultProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import com.cagataykolus.productapp.data.repository.DefaultReviewRepository
import com.cagataykolus.productapp.data.repository.ProductRepository
import com.cagataykolus.productapp.data.repository.ReviewRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ProductRepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindProductRepository(repository: DefaultProductRepository): ProductRepository
}