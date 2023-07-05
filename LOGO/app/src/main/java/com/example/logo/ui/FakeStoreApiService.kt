import com.example.logo.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {
    @GET("products/{id}")
    fun getProduct(@Path("id") id: Int): Call<Product>
}
