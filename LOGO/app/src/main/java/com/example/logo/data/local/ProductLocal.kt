package com.example.logo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductLocal (
    @PrimaryKey val id: Int,
    val title: String,
    val price: Float,
    val description: String,
    val image: String
)