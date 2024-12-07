package com.example.practica3.api

import java.io.Serializable

data class Producto (
    val id: Long,
    val nombre: String,
    val precio: Double
) : Serializable
