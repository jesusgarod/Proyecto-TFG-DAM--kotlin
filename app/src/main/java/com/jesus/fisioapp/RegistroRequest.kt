package com.jesus.fisioapp

data class RegistroRequest(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val password: String
)
