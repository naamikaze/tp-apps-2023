package com.example.logo.data

import android.content.Context
import android.util.Log
import com.example.logo.data.local.AppDatabase
import com.example.logo.data.local.toExternal
import com.example.logo.data.local.toLocal
import com.example.logo.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsDataSource {

    private val _BASE_URL = "https://fakestoreapi.com/"
    private val _TAG = "API-DEMO"

    suspend fun getProducts(id: Int, context: Context): ArrayList<Product> {
        Log.d(_TAG, "Productos DataSource Get")

        Log.d(_TAG, "Busco lista local")
        var  pLocal = AppDatabase.getInstance(context).productsDAO().getAll()
        if (pLocal.size > 0){
            Log.d(_TAG, "Devuelvo lista local")
            return pLocal.toExternal() as ArrayList<Product>

        }

        val api = Retrofit.Builder()
            .baseUrl(_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsAPI::class.java)

        val response = api.getProducts(id).execute()

        return if (response.isSuccessful) {
            Log.d(_TAG, "Resultado Exitoso!")
            var pList = response.body() ?: ArrayList()

            if (pList.size > 0) {
                Log.d(_TAG, "Guardo lista local")
                var pListLocal = pList.toLocal().toTypedArray()
                AppDatabase.getInstance(context).productsDAO().insert(*pListLocal)
            }

            pList
        } else {
            Log.e(_TAG, "Error en llamado API: ${response.message()}")
            ArrayList()
        }
    }

    suspend fun searchProductById(id: Int): Product? {
        Log.d(_TAG, "Productos DataSource Search by Id")

        val api = Retrofit.Builder()
            .baseUrl(_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsAPI::class.java)

        val response = api.getProductById(id).execute()

        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.e(_TAG, "Error en llamado API: ${response.message()}")
            null
        }
    }
}
