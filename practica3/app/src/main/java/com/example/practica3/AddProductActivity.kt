package com.example.practica3

import com.example.practica3.api.ApiClient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practica3.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductActivity : ComponentActivity() {

    private lateinit var textProductName: EditText
    private lateinit var textProductPrice: EditText
    private lateinit var buttonAddProduct: Button
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        textProductName = findViewById(R.id.addTextProductName)
        textProductPrice = findViewById(R.id.addTextProductPrice)
        buttonAddProduct = findViewById(R.id.addButtonAddProduct)


        backToCatalog()

        //Añadir producto
        addProduct()
    }

    private fun addProduct() {
        buttonAddProduct.setOnClickListener {
            val productName = textProductName.text.toString()
            val productPrice = textProductPrice.text.toString()

            if (productName.isEmpty() || productPrice.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                apiService.addProducto(productName, productPrice.toDouble())
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@AddProductActivity,
                                    "$productName añadido con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@AddProductActivity,
                                    "Error al añadir el producto",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                this@AddProductActivity,
                                "Error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun backToCatalog() {
        val buttonBack: Button = findViewById(R.id.addBackToCatalog)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
