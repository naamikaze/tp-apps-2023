package com.example.logo.data

import com.example.logo.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsAPI {
    @GET("/products")
    fun getProducts(
        @Query("id") id: Int
    ): Call<ArrayList<Product>>

    @GET("/products/{id}")
    fun getProductById(
        @Path("id") id: Int
    ): Call<Product>
}
