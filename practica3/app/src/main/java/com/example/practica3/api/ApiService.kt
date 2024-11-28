package com.example.practica3.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/productos")
    fun getAllProducts(): Call<List<Producto>>

    /*

    @POST("/products/add")
    fun addProduct(
        @Query("name") name: String,
        @Query("price") price: Double
    ): Call<Void>

    @POST("products/edit/{id}")
    fun editProduct(
        @Path("id") id: Long,
        @Query("name") name: String,
        @Query("price") price: Double
    ): Call<Void>

    @POST("products/delete/{id}")
    fun deleteProduct(
        @Path("id") id: Long
    ): Call<Void>

     */

}