package com.cagataykolus.productapp.di.module

import android.app.Application
import com.cagataykolus.productapp.data.local.ReviewDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

@InstallIn(SingletonComponent::class)
@Module
class ReviewDatabaseModule {
    @Singleton
    @Provides
    fun provideReviewDatabase(application: Application) = ReviewDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideReviewDao(database: ReviewDatabase) = database.getReviewDao()
}
