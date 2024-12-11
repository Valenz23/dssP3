package com.example.practica3.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/productos")
    fun getAllProducts(): Call<List<Producto>>

    @GET("/api/carrito")
    fun getCartItems(): Call<List<Producto>>

    @POST("/api/carrito/add")
    fun addToCart(@Body producto: Producto): Call<Void>

    @DELETE("api/carrito/remove/{id}")
    fun removeFromCart(@Path("id") productId: Long): Call<Void>

    @POST("api/carrito/clear")
    fun clearCart(): Call<Void>

    @POST("api/carrito/comprar")
    fun checkout(): Call<Void>

    @POST("api/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<Map<String, String>>

}