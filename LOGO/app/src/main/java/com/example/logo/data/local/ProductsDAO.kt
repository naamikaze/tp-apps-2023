package com.example.logo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductsDAO {

    @Query("SELECT * FROM products")
    fun getAll() : List<ProductLocal>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getById(id: Int) : ProductLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg product: ProductLocal)

    @Delete
    fun delete(product: ProductLocal)

}