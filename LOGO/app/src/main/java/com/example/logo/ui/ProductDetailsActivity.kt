package com.example.logo.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.logo.R
import com.example.logo.model.FavouriteProduct
import com.example.logo.model.Product
import com.example.logo.model.Rating
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var product: Product
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        // Obtener el producto de los datos extras del Intent
        product = intent.getParcelableExtra("product") ?: Product(0, "", 0.0f, "", "", "", Rating(0.0f, 0))

        // Mostrar los detalles del producto en la vista
        showProductDetails()

        // Actualizar el título de la barra de acción con el nombre del producto
        supportActionBar?.title = product.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showProductDetails() {
        val btnAddToFavorites: Button = findViewById(R.id.btnAddToFavorites)
        btnAddToFavorites.setOnClickListener {
            addToFavorites(product)
        }

        val imgProductDetails: ImageView = findViewById(R.id.imgProductDetails)
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvPrice: TextView = findViewById(R.id.tvPrice)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        // Obtén la instancia de FirebaseAuth para obtener el ID del usuario actual
        val firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser?.uid ?: ""

        // Asignar los valores del producto a los elementos de la vista
        tvTitle.text = product.title
        tvPrice.text = "$${product.price}"
        tvDescription.text = product.description

        Glide.with(this)
            .load(product.image)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(imgProductDetails)
    }

    private fun addToFavorites(product: Product) {
        // Crea una instancia de FavouriteProduct con los datos necesarios
        val favouriteProduct = FavouriteProduct(product.id, userId)

        // Obtiene una referencia a la colección "users" en Firestore
        val firestore = FirebaseFirestore.getInstance()
        val usersCollection = firestore.collection("users")

        // Obtiene una referencia al documento del usuario actual
        val currentUserDocRef = usersCollection.document(userId)

        // Agrega el ID del producto a la colección "favorite_products" del usuario
        currentUserDocRef.collection("favorite_products").document(product.id.toString()).set(favouriteProduct)
            .addOnSuccessListener {
                // Guardado exitoso, puedes mostrar un mensaje o realizar alguna acción adicional
                Toast.makeText(this, "Producto agregado a favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Error al guardar, maneja el error de acuerdo a tus necesidades
                Toast.makeText(this, "Error al agregar el producto a favoritos", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Volver atrás cuando se hace clic en la flecha de retroceso
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
