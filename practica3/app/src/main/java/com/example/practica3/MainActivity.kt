package com.example.practica3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.api.ApiClient
import com.example.practica3.api.ApiService
import com.example.practica3.api.Producto
import com.example.practica3.api.ProductAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("role", "unknown")  // Por defecto, "unknown" si no hay rol guardado

        // Encontramos el TextView para mostrar el rol
        val roleTextView: TextView = findViewById(R.id.textViewRole)

        // Actualizamos el contenido del TextView con el rol
        roleTextView.text = "Rol: $role"

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //botón Ir a carrito
        showCart()

        //boton Mapa
        showMap()

        // llamada a la api para mostrar los productos
        showProducts()

    }

    private fun showProducts() {
        apiService.getAllProducts().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(
                call: Call<List<Producto>>,
                response: Response<List<Producto>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { productList ->
                        productAdapter = ProductAdapter(productList, apiService, this@MainActivity)
                        recyclerView.adapter = productAdapter
                    }
                } else Log.e("API_ERROR", "Error code: ${response.code()}")
            }
            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }

    private fun showCart() {
        val buttonViewCart: Button = findViewById(R.id.buttonViewCart)
        buttonViewCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showMap() {
        val buttonViewMap: Button = findViewById(R.id.buttonViewMap)
        buttonViewMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Practica3Theme {
        Greeting("Pablerio")
    }
}
 */