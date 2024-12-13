package com.example.practica3.api

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.EditProductActivity
import com.example.practica3.MapActivity
import com.example.practica3.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductAdapter(
    private val productList: List<Producto>,
    private val apiService: ApiService,
    private val context: Context,
    private val role: String
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        val buttonAddToCart: Button = itemView.findViewById(R.id.buttonAddToCart)
        val buttonEditProduct: Button = itemView.findViewById(R.id.buttonEditProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.textViewName.text = product.nombre
        holder.textViewPrice.text = "${product.precio}€"

        if (role == "admin") {
            holder.buttonEditProduct.visibility = View.VISIBLE
            editProduct(holder, product.id)
        } else {
            holder.buttonEditProduct.visibility = View.GONE
        }

        addToCart(holder, product)

    }

    private fun editProduct(
        holder: ProductViewHolder,
        id: Long
    ) {
        holder.buttonEditProduct.setOnClickListener {
            val intent = Intent(context, EditProductActivity::class.java).apply {
                putExtra("id", id)
            }
            context.startActivity(intent)
        }
    }

    private fun addToCart(
        holder: ProductViewHolder,
        product: Producto
    ) {
        holder.buttonAddToCart.setOnClickListener {

            apiService.addToCart(product).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "${product.nombre} añadido al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(context, "Error al añadir al carrito", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}

