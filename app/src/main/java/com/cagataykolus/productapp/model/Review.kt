package com.cagataykolus.productapp.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

@Parcelize
@Entity(tableName = Review.TABLE_REVIEW)
data class Review(
    @NonNull
    val productId: String,
    val locale: String?,
    val rating: Int,
    @PrimaryKey
    // Review entity should contain reviewId as PrimaryKey. 'text' field is Primary Key for now.
    val text: String
) : Parcelable {
    companion object {
         const val TABLE_REVIEW = "table_review"
    }
}