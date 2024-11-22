package com.example.practica3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.api.ApiClient
import com.example.practica3.api.ApiService
import com.example.practica3.api.Product
import com.example.practica3.api.ProductAdapter
import com.example.practica3.ui.theme.Practica3Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        enableEdgeToEdge()
        setContent {
            Practica3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        */

        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productAdapter = ProductAdapter(sampleProducts)
        recyclerView.adapter = productAdapter

        //Ecsample API Cal
        apiService.getAllProducts().enqueue(object : Callback<List<Product>> {

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful){
                    val data = response.body()
                    data?.let { productList ->
                        productAdapter = ProductAdapter(productList)
                        recyclerView.adapter = productAdapter
                    }
                }
                else Log.e("API_ERROR", "Error code: ${response.code()}")
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable){
                Log.e("API_ERROR", "Failure: ${t.message}")
            }

        })

    }
}

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