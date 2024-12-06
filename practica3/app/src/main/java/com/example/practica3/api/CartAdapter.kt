package com.example.practica3.api

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartAdapter(
    private val cartList: MutableList<Producto>,
    private val apiService: ApiService,
    private val context: Context,
    private val onTotalUpdated: (Double) -> Unit
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewCartName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewCartPrice)
        val buttonRemoveFromCart: Button = itemView.findViewById(R.id.buttonRemoveFromCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val product = cartList[position]
        holder.textViewName.text = product.nombre
        holder.textViewPrice.text = "${product.precio}€"

        holder.buttonRemoveFromCart.setOnClickListener {
            apiService.removeFromCart(product.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        cartList.removeAt(position)
                        notifyItemRemoved(position)
                        val updatedTotal = calculateTotal()
                        onTotalUpdated(updatedTotal)
                        Toast.makeText(context, "${product.nombre} eliminado del carrito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al eliminar del carrito", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount(): Int = cartList.size

    private fun calculateTotal(): Double {
        return cartList.sumOf { it.precio }
    }
}