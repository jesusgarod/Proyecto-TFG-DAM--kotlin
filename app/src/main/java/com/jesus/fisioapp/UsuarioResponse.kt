package com.jesus.fisioapp

data class UsuarioResponse(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val edad: Int,
    val rol: String
)
