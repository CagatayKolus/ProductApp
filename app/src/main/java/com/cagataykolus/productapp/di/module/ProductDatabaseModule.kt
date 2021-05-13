package com.cagataykolus.productapp.di.module

import android.app.Application
import com.cagataykolus.productapp.data.local.ProductDatabase
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
class ProductDatabaseModule {
    @Singleton
    @Provides
    fun provideProductDatabase(application: Application) = ProductDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideProductDao(database: ProductDatabase) = database.getProductDao()
}
