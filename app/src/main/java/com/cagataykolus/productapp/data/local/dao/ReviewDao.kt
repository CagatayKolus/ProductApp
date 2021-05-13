package com.cagataykolus.productapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import kotlinx.coroutines.flow.Flow

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Data Access Object (DAO) for [com.cagataykolus.productapp.data.local.ReviewDatabase]
 */
@Dao
interface ReviewDao {
    /**
     * Inserts [reviews] into the [Review.TABLE_REVIEW] table.
     * Duplicate values are replaced in the table.
     * @param reviews
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReviews(reviews: List<Review>)

    /**
     * Inserts [review] into the [Review.TABLE_REVIEW] table.
     * Duplicate values are replaced in the table.
     * @param review
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReview(review: Review)

    /**
     * Deletes all the data from the [Review.TABLE_REVIEW] table.
     */
    @Query("DELETE FROM ${Review.TABLE_REVIEW}")
    suspend fun deleteAllReviews()

    /**
     * Fetches the data from the [Review.TABLE_REVIEW] table whose value is [productId].
     * @return [Flow] of [Review] from database table.
     */
    @Query("SELECT * FROM ${Review.TABLE_REVIEW} WHERE productId = :productId")
    fun getReviewByName(productId: String): Flow<Review>

    /**
     * Fetches the data from the [Review.TABLE_REVIEW] table whose value is [text].
     * @return [Flow] of [Review] from database table.
     */
    @Query("SELECT * FROM ${Review.TABLE_REVIEW} WHERE text = :text")
    fun getReviewByText(text: String): Flow<Review>

    /**
     * Fetches all the data from the [Review.TABLE_REVIEW] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${Review.TABLE_REVIEW}")
    fun getAllReviews(): Flow<List<Review>>
}