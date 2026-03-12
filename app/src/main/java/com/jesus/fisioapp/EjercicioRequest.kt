package com.jesus.fisioapp

data class EjercicioRequest(
    val nombre: String,
    val descripcion: String,
    val series: Int,
    val repeticiones: Int,
    val urlVideo: String? = null
)
