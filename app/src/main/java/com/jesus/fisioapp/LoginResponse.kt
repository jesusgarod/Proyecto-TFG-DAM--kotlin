package com.jesus.fisioapp


//Aqui se recoge el token que nos da Spring Boot
data class LoginResponse(
    val token : String,
    val rol: String? = null
)
