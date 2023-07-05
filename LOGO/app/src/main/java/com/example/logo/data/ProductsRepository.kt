package com.example.logo.data

import android.content.Context
import com.example.logo.model.Product

class ProductsRepository {

    private val productsDS = ProductsDataSource()

    suspend fun getProducts(id: Int, context: Context): ArrayList<Product> {
        return productsDS.getProducts(id, context)
    }

    suspend fun searchProductById(id: Int): Product? {
        return productsDS.searchProductById(id)
    }
}
