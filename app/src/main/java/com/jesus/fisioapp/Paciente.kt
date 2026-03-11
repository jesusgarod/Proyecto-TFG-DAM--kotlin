package com.jesus.fisioapp

data class Paciente(
    val id: Long,
    val nombre: String,
    val apellidos: String?,
    val edad : Int,
    val email: String,
    val rol: String?
)
