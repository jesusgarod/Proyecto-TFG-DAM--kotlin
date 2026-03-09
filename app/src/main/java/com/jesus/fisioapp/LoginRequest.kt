package com.jesus.fisioapp


//Datos que le mandamos a spring boot para iniciar sesion
data class LoginRequest(
    val email : String,
    val password : String
)
