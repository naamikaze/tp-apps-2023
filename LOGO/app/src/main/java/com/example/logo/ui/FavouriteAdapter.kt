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
import com.example.logo.model.FavouriteProduct
import com.example.logo.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavouriteAdapter(
    private val favourites: List<FavouriteProduct>,
    private val onRemoveClickListener: OnRemoveClickListener
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/") // URL base de la API
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(FakeStoreApiService::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favourite = favourites[position]

        val call = apiService.getProduct(favourite.productId)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    product?.let {
                        holder.titleTextView.text = product.title
                        holder.priceTextView.text = "$${product.price}"
                        loadImage(product.image, holder.imageView)
                    }
                } else {
                    // Handle API error response
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                // Handle API call failure
            }
        })

        holder.productIdTextView.text = favourite.productId.toString()
        holder.removeButton.setOnClickListener {
            onRemoveClickListener.onRemoveClick(favourite) // Notify the listener when remove button is clicked
        }
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productIdTextView: TextView = itemView.findViewById(R.id.productIdTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val removeButton: Button = itemView.findViewById(R.id.removeButton)
    }

    private fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(imageView)
    }

    interface OnRemoveClickListener {
        fun onRemoveClick(favourite: FavouriteProduct)
    }
}
