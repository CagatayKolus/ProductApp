package com.cagataykolus.productapp.data.repository

import androidx.annotation.MainThread
import com.cagataykolus.productapp.data.local.dao.ProductDao
import com.cagataykolus.productapp.data.remote.api.ProductApiService
import com.cagataykolus.productapp.model.DeleteStatus
import com.cagataykolus.productapp.model.Product
import com.cagataykolus.productapp.model.Review
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Çağatay Kölüş on 11.05.2021.
 * cagataykolus@gmail.com
 */

interface ProductRepository {
    fun getAllProducts(): Flow<Resource<List<Product>>>
    fun getProductById(productId: String): Flow<Product>
    fun deleteProduct(productId: String): Flow<Resource<Product>>
    fun addProduct(
        productId: String,
        productName: String,
        productDescription: String
    ): Flow<Resource<Product>>
    fun editProduct(
        productId: String,
        productName: String,
        productDescription: String
    ): Flow<Resource<Product>>
}

/**
 * Singleton repository for fetching data from remote and storing it in database
 * for offline capability. This is single source of data.
 */
@ExperimentalCoroutinesApi
class DefaultProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val service: ProductApiService
) : ProductRepository {
    /**
     * Fetched the data from network and stored it in database. At the end, data from persistence
     * storage is fetched and emitted.
     */
    override fun getAllProducts(): Flow<Resource<List<Product>>> {
        return object : NetworkBoundRepository<List<Product>, List<Product>>() {

            override suspend fun saveRemoteData(response: List<Product>) =
                productDao.addProducts(response)

            override fun fetchFromLocal(): Flow<List<Product>> = productDao.getAllProducts()

            override suspend fun fetchFromRemote(): Response<List<Product>> = service.getProducts()
        }.asFlow()
    }

    @MainThread
    override fun getProductById(productId: String): Flow<Product> =
        productDao.getProductById(productId).distinctUntilChanged()

    override fun deleteProduct(productId: String): Flow<Resource<Product>> {
        return object : NetworkBoundRepository<Product, DeleteStatus>() {
            override suspend fun saveRemoteData(response: DeleteStatus) {
                productDao.deleteProductById(productId)
            }

            override fun fetchFromLocal(): Flow<Product> = productDao.getProductById(productId)

            override suspend fun fetchFromRemote(): Response<DeleteStatus> {
                productDao.deleteProductById(productId)
                return service.deleteProduct(productId)
            }

        }.asFlow()
    }

    override fun addProduct(
        productId: String,
        productName: String,
        productDescription: String
    ): Flow<Resource<Product>> {
        return object : NetworkBoundRepository<Product, Product>() {
            override suspend fun saveRemoteData(response: Product) {
                productDao.addProduct(response)
            }

            override fun fetchFromLocal(): Flow<Product> = productDao.getProductByName(productName)

            override suspend fun fetchFromRemote(): Response<Product>  =
                // currency and price are ignored for update.
                // Because Product API Service doesn't have any update feature for price and currency.
                service.addProduct(
                    Product(
                        id = productId,
                        name = productName,
                        description = productDescription,
                        currency = "",
                        price = 0
                    )
                )

        }.asFlow()
    }

    override fun editProduct(
        productId: String,
        productName: String,
        productDescription: String
    ): Flow<Resource<Product>> {
        return object : NetworkBoundRepository<Product, Product>() {
            override suspend fun saveRemoteData(response: Product) {
                productDao.editProduct(productId, productName, productDescription)
            }

            override fun fetchFromLocal(): Flow<Product> = productDao.getProductById(productId)


            override suspend fun fetchFromRemote(): Response<Product> {
                productDao.deleteProductById(productId)
                // currency and price are ignored for update.
                // Because Product API Service doesn't have any update feature for price and currency.
                return service.editProduct(
                    productId = productId,
                    Product(
                        id = productId,
                        name = productName,
                        description = productDescription,
                        currency = "",
                        price = 0
                    )
                )
            }

        }.asFlow()
    }
}