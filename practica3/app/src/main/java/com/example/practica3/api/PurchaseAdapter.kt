package com.example.practica3.api

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3.R
import com.example.practica3.api.Producto

class PurchaseAdapter(
    private val cartItems: List<Producto>
) : RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchase_item, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val product = cartItems[position]
        holder.textViewName.text = product.nombre
        holder.textViewPrice.text = "${product.precio}€"
    }

    override fun getItemCount(): Int = cartItems.size

    inner class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
    }
}
