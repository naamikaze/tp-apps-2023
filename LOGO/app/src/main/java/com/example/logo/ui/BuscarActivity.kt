package com.example.logo.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.logo.R
import com.example.logo.data.ProductsRepository
import com.example.logo.model.Product
import com.example.logo.ui.ProductItemClickListener
import com.example.logo.ui.ProductsAdapter
import kotlinx.coroutines.*

class BuscarActivity : AppCompatActivity(), ProductItemClickListener {
    private lateinit var searchView: SearchView
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var adapter: ProductsAdapter
    private val repository = ProductsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Buscar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_buscar)

        bindViews()
        setupRecyclerView()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Si deseas realizar la búsqueda en tiempo real mientras el usuario escribe,
                // puedes descomentar la siguiente línea y eliminar el método onQueryTextSubmit.
                // performSearch(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindViews() {
        searchView = findViewById(R.id.searchView)
        rvSearchResults = findViewById(R.id.rvSearchResults)
    }

    private fun setupRecyclerView() {
        rvSearchResults.layoutManager = LinearLayoutManager(this)
        adapter = ProductsAdapter()
        adapter.setOnItemClickListener(this)
        rvSearchResults.adapter = adapter
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            // Cancelamos la búsqueda anterior si aún está en progreso
            GlobalScope.coroutineContext.cancelChildren()

            // Mostramos un indicador de carga mientras se realiza la búsqueda
            adapter.items = emptyList()

            val job = GlobalScope.launch(Dispatchers.IO) {
                val id = query.toIntOrNull()
                val result: Product? = if (id != null) {
                    repository.searchProductById(id)
                } else {
                    null
                }

                withContext(Dispatchers.Main) {
                    adapter.items = if (result != null) {
                        listOf(result)
                    } else {
                        emptyList()
                    }
                }
            }

            // Si deseas agregar un tiempo de espera antes de mostrar los resultados,
            // puedes descomentar las siguientes líneas y ajustar el tiempo en milisegundos.
            /*
            val timeout = 500L
            GlobalScope.launch(Dispatchers.Main) {
                delay(timeout)
                if (job.isActive) {
                    job.cancel()
                    // Mostrar un mensaje de "No se encontraron resultados" o alguna otra acción
                }
            }
            */
        } else {
            adapter.items = emptyList()
        }
    }

    override fun onProductItemClick(position: Int) {
        val product = adapter.items[position]
        // Realiza la acción que deseas al hacer clic en un producto (por ejemplo, abrir los detalles)
    }
}
