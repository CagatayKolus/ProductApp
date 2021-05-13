package com.cagataykolus.productapp.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cagataykolus.productapp.model.Product.Companion.TABLE_PRODUCT
import kotlinx.android.parcel.Parcelize

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

@Parcelize
@Entity(tableName = TABLE_PRODUCT)
data class Product(
    @PrimaryKey
    @NonNull
    val id: String,
    val name: String,
    val description: String,
    val currency: String,
    val price: Int,
) : Parcelable {
    companion object {
        const val TABLE_PRODUCT = "table_product"
    }
}
