package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.api.ApiClient
import com.example.practica3.api.ApiService
import com.example.practica3.api.CartAdapter
import com.example.practica3.api.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private lateinit var textViewTotal: TextView
    private var cartList: MutableList<Producto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        textViewTotal = findViewById(R.id.textViewTotal)

        // productos en el carrito
        loadCartItems()

        // botón volver al catálogo
        backToCatalog()

        // botón limpiar el carrito
        clearCart()

        //botón comprar
        checkout()
    }

    private fun clearCart() {
        val buttonClearCart: Button = findViewById(R.id.buttonClearCart)
        buttonClearCart.setOnClickListener {
            val buttonBuy: Button = findViewById(R.id.buttonBuy)
            apiService.clearCart().enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        cartList.clear()
                        cartAdapter.notifyDataSetChanged()
                        updateTotal(cartList)
                        buttonBuy.isEnabled = false
                        buttonBuy.backgroundTintList = ContextCompat.getColorStateList(this@CartActivity, R.color.purple_200)
                        Toast.makeText(this@CartActivity, "Carrito limpiado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CartActivity, "Error al limpiar el carrito", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateTotal(cartItems: List<Producto>) {
        val total = cartItems.sumOf { it.precio }
        textViewTotal.text = "Total: ${"%.2f".format(total)}€"
    }

    private fun loadCartItems(){
        val buttonBuy: Button = findViewById(R.id.buttonBuy)
        apiService.getCartItems().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    cartList = response.body()?.toMutableList() ?: mutableListOf()
                    cartAdapter = CartAdapter(cartList, apiService, this@CartActivity) { updatedTotal ->
                        textViewTotal.text = "Total: ${"%.2f".format(updatedTotal)}€"
                    }
                    recyclerView.adapter = cartAdapter
                    updateTotal(cartList)
                    if (cartList.isEmpty()) {
                        buttonBuy.isEnabled = false
                        buttonBuy.backgroundTintList = ContextCompat.getColorStateList(this@CartActivity, R.color.purple_200)
                    } else {
                        buttonBuy.isEnabled = true
                        buttonBuy.backgroundTintList = ContextCompat.getColorStateList(this@CartActivity, R.color.purple_500)
                    }
                }
                else {
                    Toast.makeText(this@CartActivity, "Error al cargar el carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkout() {
        val buttonBuy: Button = findViewById(R.id.buttonBuy)
        buttonBuy.setOnClickListener {
            val intent = Intent(this, ThankYouActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun backToCatalog() {
        val buttonViewMain: Button = findViewById(R.id.buttonViewMain)
        buttonViewMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}