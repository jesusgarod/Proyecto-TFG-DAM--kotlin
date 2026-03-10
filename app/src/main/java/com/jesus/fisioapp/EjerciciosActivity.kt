package com.jesus.fisioapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EjerciciosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ejercicios)



        val rvEjercicios = findViewById<RecyclerView>(R.id.rvEjercicios)

        //organiza una tarjeta debajo de la otra
        rvEjercicios.layoutManager = LinearLayoutManager(this)


//        val listaPrueba = listOf(
//            Ejercicio("Estiramiento de cuello", "Gira la cabeza lentamente lado a lado durante 30 segs"),
//            Ejercicio("Rotacion de hombros","Haz cirulos hacia atrás con los hombbros 10 segs"),
//            Ejercicio ("Elevación de gemelos","Ponte de puntillas apoyado en una silla y sube y baja lentamente 10 veces")
//        )


        val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
        val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)

        if (tokenGuardado != null) {
            val tokenConBearer = "Bearer $tokenGuardado"

            //se lee el id que guardamos en el menu principal
            val idPaciente = blocDeNotas.getLong("ID_PACIENTE", 1L)

            // Llamo al servidor pidiendo la lista de ejercicios
            RetrofitClient.apiService.obtenerMisEjercicios(tokenConBearer, idPaciente).enqueue(object : retrofit2.Callback<List<Ejercicio>> {

                override fun onResponse(call: retrofit2.Call<List<Ejercicio>>, response: retrofit2.Response<List<Ejercicio>>) {
                    if (response.isSuccessful) {

                        val listaReal = response.body()

                        if (listaReal != null) {
                            // 3. Contratamos al trabajador y le pasamos la lista de verdad
                            val adaptador = EjercicioAdapter(listaReal)
                            // Enganchamos el trabajador al tablón
                            rvEjercicios.adapter = adaptador
                        }
                    } else {
                        Toast.makeText(this@EjerciciosActivity, "Error al cargar tus rutinas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Ejercicio>>, t: Throwable) {
                    Toast.makeText(this@EjerciciosActivity, "Fallo de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(this, "No se encontró tu sesión", Toast.LENGTH_SHORT).show()
        }

//        val adaptador = EjercicioAdapter(listaPrueba)

//        rvEjercicios.adapter = adaptador


    }
}