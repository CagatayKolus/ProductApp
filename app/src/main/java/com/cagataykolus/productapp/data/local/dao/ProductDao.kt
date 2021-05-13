package com.cagataykolus.productapp.data.local.dao

import androidx.room.*
import com.cagataykolus.productapp.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

/**
 * Data Access Object (DAO) for [com.cagataykolus.productapp.data.local.ProductDatabase]
 */
@Dao
interface ProductDao {
    /**
     * Inserts [products] into the [Product.TABLE_PRODUCT] table.
     * Duplicate values are replaced in the table.
     * @param products
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(products: List<Product>)

    /**
     * Deletes all the data from the [[Product.TABLE_PRODUCT] table.
     */
    @Query("DELETE FROM ${Product.TABLE_PRODUCT}")
    suspend fun deleteAllProducts()

    /**
     * Deletes where [Product.id] = [productId] from the [[Product.TABLE_PRODUCT] table.
     */
    @Query("DELETE FROM ${Product.TABLE_PRODUCT} WHERE id = :productId")
    fun deleteProductById(productId: String)

    /**
     * Inserts [product] into the [Product.TABLE_PRODUCT] table.
     * Duplicate values are replaced in the table.
     * @param product
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product)

    /**
     * Updates where [Product.id] = [productId] from the [Product.TABLE_PRODUCT] table.
     * @param product
     */
    @Query("UPDATE ${Product.TABLE_PRODUCT} set name = :productName, description = :productDescription WHERE id = :productId")
    fun editProduct(productId: String, productName: String, productDescription: String)

    /**
     * Fetches the data from the [Product.TABLE_PRODUCT] table whose value is [productId].
     * @param productId is unique ID of [Product]
     * @return [Flow] of [Product] from database table.
     */
    @Query("SELECT * FROM ${Product.TABLE_PRODUCT} WHERE id = :productId")
    fun getProductById(productId: String): Flow<Product>

    /**
     * Fetches the data from the [Product.TABLE_PRODUCT] table whose value is [productName].
     * @return [Flow] of [Product] from database table.
     */
    @Query("SELECT * FROM ${Product.TABLE_PRODUCT} WHERE name = :productName")
    fun getProductByName(productName: String): Flow<Product>

    /**
     * Fetches all the data from the [Product.TABLE_PRODUCT] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${Product.TABLE_PRODUCT}")
    fun getAllProducts(): Flow<List<Product>>
}