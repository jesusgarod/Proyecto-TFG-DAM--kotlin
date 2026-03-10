package com.jesus.fisioapp

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

//Se definen las rutas del servidor a las que la app llamará
interface FisioApiService {

    //ruta para el login
    @POST("/api/usuarios/login")
    fun iniciarSesion(@Body request: LoginRequest): Call<LoginResponse>


    @GET("/api/usuarios/perfil")
    fun obtenerPerfilUsuario( @Header("Authorization") token: String): Call<UsuarioResponse>

    // para enviar los datos del registro
    @POST("/api/usuarios/registro")
    fun registrarPaciente(@Body request: RegistroRequest): Call<Void>

    @GET("/api/ejercicios/usuario/{id}")
    fun obtenerMisEjercicios(@Header("Authorization") token: String, @Path("id") idPaciente: Long): retrofit2.Call<List<Ejercicio>>
}