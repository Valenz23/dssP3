package com.example.practica3

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.api.ApiClient
import com.example.practica3.api.ApiService
import com.example.practica3.api.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThankYouActivity : ComponentActivity() {

    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)

        checkout()

        backToCatalog()
    }

    private fun checkout() {
        apiService.checkout().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // La compra fue exitosa, puedes hacer cualquier acción adicional aquí
                    //Toast.makeText(this@ThankYouActivity, "Compra realizada", Toast.LENGTH_SHORT).show()
                } else {
                    // Manejar cualquier error de la API
                    Toast.makeText(
                        this@ThankYouActivity,
                        "Error al procesar la compra",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Error en la llamada a la API
                Toast.makeText(
                    this@ThankYouActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun backToCatalog() {
        val buttonBackToCatalog: Button = findViewById(R.id.buttonBackToCatalog)
        buttonBackToCatalog.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}