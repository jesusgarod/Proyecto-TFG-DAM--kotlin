package com.jesus.fisioapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AsignarEjercicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_asignar_ejercicio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNombreEjercicio = findViewById<EditText>(R.id.etNombreEjercicio)
        val etDescripcionEjercicio = findViewById<EditText>(R.id.etDescripcionEjercicio)
        val etSeries = findViewById<EditText>(R.id.etSeries)
        val etRepeticiones = findViewById<EditText>(R.id.etRepeticiones)
        val btnEnviarEjercicio = findViewById<Button>(R.id.btnEnviarEjercicio)
        val etEnlaceVideo = findViewById<EditText>(R.id.etUrlVideo)

        val idPaciente = intent.getLongExtra("ID_PACIENTE_DESTINO", -1L)

        btnEnviarEjercicio.setOnClickListener {

            val nombreEscrito = etNombreEjercicio.text.toString()
            val descripcionEscrita = etDescripcionEjercicio.text.toString()
            val seriesEscritas = etSeries.text.toString().toIntOrNull() ?: 0 //para que si no se pone nada que por defecto sea 0 y no falle la app
            val repeticionesEscritas = etRepeticiones.text.toString().toIntOrNull() ?: 0
            val enlaceEscrito = etEnlaceVideo.text.toString()


            if (nombreEscrito.isEmpty() || descripcionEscrita.isEmpty()){

                Toast.makeText(this, "Por favor, rellena el nombre y la descripción", Toast.LENGTH_SHORT).show()

            }else if (idPaciente != -1L){

                val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
                val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)

                if (tokenGuardado != null){
                    val tokenConBearer = "Bearer $tokenGuardado"

                    val peticionEjercicio = EjercicioRequest(
                        nombre = nombreEscrito,
                        descripcion = descripcionEscrita,
                        series = seriesEscritas,
                        repeticiones = repeticionesEscritas,
                        urlVideo = enlaceEscrito

                    )

                    RetrofitClient.apiService.asignarEjercicio(tokenConBearer, idPaciente, peticionEjercicio)
                        .enqueue(object : retrofit2.Callback<Ejercicio> {

                            override fun onResponse(call: retrofit2.Call<Ejercicio>, response: retrofit2.Response<Ejercicio>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(this@AsignarEjercicioActivity, "¡Ejercicio asignado con éxito!", Toast.LENGTH_SHORT).show()
                                    finish() // Cerramos la pantalla
                                } else {
                                    Toast.makeText(this@AsignarEjercicioActivity, "Error al guardar: código ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<Ejercicio>, t: Throwable) {
                                Toast.makeText(this@AsignarEjercicioActivity, "Error de conexión", Toast.LENGTH_LONG).show()
                            }
                        })
                }
            }

        }

    }





}