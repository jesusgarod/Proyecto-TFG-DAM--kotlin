package com.jesus.fisioapp

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("/api/citas/crear/{id}")
    fun solicitarCita(@Header("Authorization") token: String, @Path("id") idPaciente: Long, @Body citaNueva: Cita): retrofit2.Call<Cita>


    @GET("/api/citas/usuario/{id}")
    fun obtenerMisCitas (@Header ("Authorization") token: String, @Path("id") idPaciente: Long) : retrofit2.Call<List<Cita>>


    @DELETE("/api/citas/cancelar/{id}")
    fun eliminarCita (@Header("Authorization") token: String, @Path("id") idPaciente: Long) : retrofit2.Call<Void> //void para que al borrar el server no nos de otra cita

    @GET("/api/usuarios/todos")
    fun obtenerTodosLosPacientes (@Header("Authorization") token: String) : retrofit2.Call<List<Paciente>>

    @POST("/api/ejercicios/asignar/{id}")
    fun asignarEjercicio (@Header ("Authorization") token: String,
                          @Path("id") idPaciente: Long,
                          @Body ejercicio: EjercicioRequest) : retrofit2.Call<Ejercicio> //devuelve el ejercicio ya guardado

    @DELETE("/api/ejercicios/{id}")
    fun eliminarEjercicio (@Header ("Authorization") token: String, @Path("id") idEjercicio: Long): retrofit2.Call<Void>


    @GET("/api/ejercicios/usuario/{id}")
    fun obtenerEjerciciosPaciente (@Header ("Authorization") token: String, @Path("id") idPaciente: Long): retrofit2.Call<List<Ejercicio>>

    @GET("/api/citas/todas")
    fun obtenerTodasLasCitas (@Header ("Authorization") token: String): retrofit2.Call<List<Cita>>

    @PUT("/api/citas/estado/{citaId}")
    fun actualizarEstadoCita (@Header ("Authorization") token: String, @Path("citaId") citaId: Long, @Query("estado") nuevoEstado: String) : retrofit2.Call<Cita>




































}