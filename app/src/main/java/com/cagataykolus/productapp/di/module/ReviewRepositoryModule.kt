package com.cagataykolus.productapp.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import com.cagataykolus.productapp.data.repository.DefaultReviewRepository
import com.cagataykolus.productapp.data.repository.ReviewRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

@ExperimentalCoroutinesApi
@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ReviewRepositoryModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindReviewRepository(repository: DefaultReviewRepository): ReviewRepository
}