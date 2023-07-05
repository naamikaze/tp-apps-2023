import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.logo.R
import com.example.logo.model.Product

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.lblTitle)
    val price: TextView = itemView.findViewById(R.id.lblPrice)
    val image: ImageView = itemView.findViewById(R.id.imgProduct)
    val verMasButton: Button = itemView.findViewById(R.id.btnButton)

    fun bind(product: Product) {
        title.text = product.title
        price.text = "Price: " + product.price.toString()

        val requestOptions = RequestOptions().placeholder(R.drawable.placeholder)
        Glide.with(itemView.context)
            .setDefaultRequestOptions(requestOptions)
            .load(product.image)
            .into(image)

        // Aquí establecemos la imagen del placeholder
        image.setImageResource(R.drawable.placeholder)
    }
}
