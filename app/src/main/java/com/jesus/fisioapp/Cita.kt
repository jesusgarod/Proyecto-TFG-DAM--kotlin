package com.jesus.fisioapp

data class Cita(
    val id: Long? = null,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: String = "Pendiente" //Por defecto, hasta que el fisio la confirme.
)
