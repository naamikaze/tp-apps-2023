package com.example.logo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logo.R
import com.google.firebase.auth.FirebaseAuth

interface ProductItemClickListener {
    fun onProductItemClick(position: Int)
}

class MainActivity : AppCompatActivity(), ProductItemClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductsAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private val progressDialog by lazy { CustomProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "Productos"
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        bindView()
        bindViewModel()

        adapter.setOnItemClickListener(this)

        val button2 = findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            val intent = Intent(this, BuscarActivity::class.java)
            startActivity(intent)
        }

        val button3 = findViewById<Button>(R.id.button3)
        button3.setOnClickListener {
            val intent = Intent(this, FavouriteProductsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        progressDialog.start("Recuperando datos...")
        viewModel.onStart(this)
    }

    private fun bindView() {
        rvProducts = findViewById(R.id.rvProducts)
        rvProducts.layoutManager = GridLayoutManager(this, 2)

        adapter = ProductsAdapter()
        rvProducts.adapter = adapter
    }

    private fun bindViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.products.observe(this) {
            adapter.items = it
            adapter.notifyDataSetChanged()
            progressDialog.stop()
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onProductItemClick(position: Int) {
        val product = adapter.items[position]

        val intent = Intent(this, ProductDetailsActivity::class.java).apply {
            putExtra("product", product)
        }
        startActivity(intent)
    }

    fun onVerMasButtonClick(view: View) {
        val parentView = view.parent as View
        val position = rvProducts.getChildAdapterPosition(parentView)
        onProductItemClick(position)
    }
}
