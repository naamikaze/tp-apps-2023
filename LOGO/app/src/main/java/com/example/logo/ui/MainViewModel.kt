package com.example.logo.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.logo.data.ProductsRepository
import com.example.logo.model.Product;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel() {

    //Constante
    private val _TAG = "API-DEMO"
    private val coroutineContext: CoroutineContext = newSingleThreadContext("uadedemo")
    private val scope = CoroutineScope(coroutineContext)


    //Dependencias
    private val productsRepo = ProductsRepository()

    //Propiedades
    var products = MutableLiveData<ArrayList<Product>>()
    var id = 1


    //Funciones
    fun onStart(context: Context){
        //Cargar los datos de los productos
        scope.launch {
            kotlin.runCatching {
                productsRepo.getProducts(id, context)
            }.onSuccess {
                Log.d(_TAG, "Products onSucess")
                products.postValue(it)
            }.onFailure {
                Log.e(_TAG, "Products Error: " + it)
            }

        }

    }
}