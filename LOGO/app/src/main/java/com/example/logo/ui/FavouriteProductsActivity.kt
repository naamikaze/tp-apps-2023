package com.example.logo.ui

import FavouriteAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logo.R
import com.example.logo.model.FavouriteProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavouriteProductsActivity : AppCompatActivity(), FavouriteAdapter.OnRemoveClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favouriteAdapter: FavouriteAdapter
    private val favourites: MutableList<FavouriteProduct> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_products)

        supportActionBar?.title = "Favoritos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewFavourites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        favouriteAdapter = FavouriteAdapter(favourites, this)
        recyclerView.adapter = favouriteAdapter

        val btnSignOut: Button = findViewById(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            // Cerrar sesión
            FirebaseAuth.getInstance().signOut()

            // Redirigir al usuario a la actividad de inicio de sesión o cualquier otra actividad deseada
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cierra la actividad actual para que el usuario no pueda volver atrás
        }

        fetchFavourites()
    }

    override fun onRemoveClick(favourite: FavouriteProduct) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "2G9eK1PG2FbjLcjXBjwn24zrKmw2"

        val firestore = FirebaseFirestore.getInstance()
        val favouriteDocument = firestore.collection("users")
            .document(currentUserId)
            .collection("favorite_products")
            .document(favourite.productId.toString())

        favouriteDocument.delete()
            .addOnSuccessListener {
                // Eliminación exitosa
                // Actualiza la interfaz de usuario si es necesario
                fetchFavourites()
            }
            .addOnFailureListener { exception ->
                // Manejar la falla en la eliminación del favorito
            }
    }

    private fun fetchFavourites() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "2G9eK1PG2FbjLcjXBjwn24zrKmw2"

        val firestore = FirebaseFirestore.getInstance()
        val favouritesCollection = firestore.collection("users")
            .document(currentUserId)
            .collection("favorite_products")

        favouritesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                favourites.clear()
                for (documentSnapshot in querySnapshot.documents) {
                    val favourite = documentSnapshot.toObject(FavouriteProduct::class.java)
                    favourite?.let {
                        favourites.add(it)
                    }
                }
                favouriteAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Manejar la falla en la obtención de los favoritos
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
