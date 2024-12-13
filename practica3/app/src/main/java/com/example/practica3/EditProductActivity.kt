package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.practica3.api.ApiClient
import com.example.practica3.api.ApiService
import com.example.practica3.api.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProductActivity : ComponentActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        editTextName = findViewById(R.id.editTextProductName)
        editTextPrice = findViewById(R.id.editTextProductPrice)
        buttonUpdate = findViewById(R.id.buttonUpdateProduct)
        buttonDelete = findViewById(R.id.buttonDeleteProduct)

        val productId = intent.getLongExtra("id", -1)

        backToCatalog()

        // detalles de producto
        loadProductDetails(productId)

        // Botón para actualizar el producto
        updateProduct(productId)

        // Eliminar
        deleteProduct(productId)
    }

    private fun updateProduct(productId: Long) {
        buttonUpdate.setOnClickListener {
            val newName = editTextName.text.toString()
            val newPrice = editTextPrice.text.toString().toDoubleOrNull()

            if (newName.isNotEmpty() && newPrice != null) {
                apiService.updateProducto(productId, Producto(productId, newName, newPrice))
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@EditProductActivity,
                                    "$newName actualizado",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@EditProductActivity,
                                    "Error al actualizar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                this@EditProductActivity,
                                "Error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
            val intent = Intent(this, EditProductActivity::class.java).apply {
                putExtra("id", productId)
            }
            startActivity(intent)
        }
    }

    private fun deleteProduct(productId: Long) {
        buttonDelete.setOnClickListener {
            val name = editTextName.text.toString()
            apiService.deleteProducto(productId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EditProductActivity,
                            "$name eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@EditProductActivity,
                            "Error al eliminar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@EditProductActivity,
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

    private fun backToCatalog() {
        val buttonBack: Button = findViewById(R.id.editBackToCatalog)
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadProductDetails(productId: Long) {
        apiService.getProductoById(productId).enqueue(object : Callback<Producto> {
            override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        editTextName.setText(product.nombre)
                        editTextPrice.setText(product.precio.toString())
                    }
                } else {
                    Toast.makeText(this@EditProductActivity, "Error al cargar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Producto>, t: Throwable) {
                Toast.makeText(this@EditProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}