package com.example.logo.model

data class FavouriteProduct(
    val productId: Int = 0,
    val userId: String = "",
    val title: String = "",
    val price: Float = 0.0f,
    val image: String = ""
) {
    constructor() : this(0, "", "", 0.0f, "")
}
