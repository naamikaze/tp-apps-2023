package com.example.logo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.logo.R
import com.example.logo.model.Product

class ProductsAdapter : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    var items: List<Product> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var onItemClickListener: ProductItemClickListener? = null

    fun setOnItemClickListener(listener: ProductItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (items.isNotEmpty()) Integer.MAX_VALUE else 0
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if (items.isNotEmpty()) {
            val product = items[position % items.size]
            holder.bind(product)
        }
    }
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.lblTitle)
        private val priceTextView: TextView = itemView.findViewById(R.id.lblPrice)
        private val imageView: ImageView = itemView.findViewById(R.id.imgProduct)
        private val verMasButton: Button = itemView.findViewById(R.id.btnButton)

        fun bind(product: Product) {
            titleTextView.text = product.title
            priceTextView.text = "$${product.price}"

            Glide.with(itemView)
                .load(product.image)
                .apply(RequestOptions().placeholder(R.drawable.placeholder))
                .into(imageView)

            itemView.setOnClickListener {
                onItemClickListener?.onProductItemClick(adapterPosition % items.size)
            }

            verMasButton.setOnClickListener {
                onItemClickListener?.onProductItemClick(adapterPosition % items.size)
            }
        }
    }
}
