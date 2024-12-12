package com.example.practica3.api

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL="http://10.0.2.2:8080/"

//    private fun getAuthenticatedClient(context: Context): OkHttpClient {
//        val sharedPreferences = context.getSharedPreferences("AuthPreferences", Context.MODE_PRIVATE)
//        val username = sharedPreferences.getString("username", null)
//        val password = sharedPreferences.getString("password", null)
//
//        return OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val requestBuilder = chain.request().newBuilder()
//                if (username != null && password != null) {
//                    val credentials = okhttp3.Credentials.basic(username, password)
//                    requestBuilder.header("Authorization", credentials)
//                }
//                chain.proceed(requestBuilder.build())
//            }
//            .build()
//    }
//
//    fun getRetrofitWithAuth(context: Context): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(getAuthenticatedClient(context))
//            .build()
//    }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}