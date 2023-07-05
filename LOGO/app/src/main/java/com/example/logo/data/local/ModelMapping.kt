package com.example.logo.data.local

import com.example.logo.model.Product
import com.example.logo.model.Rating

fun Product.toLocal() = ProductLocal(
    id = id,
    title = title,
    price = price,
    description = description,
    image = image
)

fun List<Product>.toLocal() = map(Product::toLocal)

fun ProductLocal.toExternal() = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = "",
    image = image,
    rating = Rating(0f, 0)
)


fun List<ProductLocal>.toExternal() = map(ProductLocal::toExternal)