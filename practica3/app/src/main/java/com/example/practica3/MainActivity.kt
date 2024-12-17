package com.example.practica3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        roleTextView.text = "Hola $role"

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //botón Ir a carrito
        showCart()

        //boton Mapa
        showMap()

        //boton logout
        logout(sharedPreferences)

        //boton "añadir producto" (solo se muestra al admin)
        addProduct(role)

        // llamada a la api para mostrar los productos
        showProducts(role)

    }

    private fun addProduct(role: String?) {
        val buttonAddProduct: Button = findViewById(R.id.buttonAddProduct)
        if (role == "admin") {
            buttonAddProduct.visibility = View.VISIBLE
        } else {
            buttonAddProduct.visibility = View.GONE
        }
        buttonAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logout(sharedPreferences: SharedPreferences) {
        val buttonLogout: Button = findViewById(R.id.buttonLogout)
        buttonLogout.setOnClickListener {
            apiService.clearCart().enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showProducts(role: String?) {
        apiService.getAllProducts().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(
                call: Call<List<Producto>>,
                response: Response<List<Producto>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let { productList ->
                        productAdapter = ProductAdapter(productList, apiService, this@MainActivity, role ?: "")
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