package com.jesus.fisioapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AgendaFisioActivity : AppCompatActivity() {

    private var tokenConBearer: String = ""
    private lateinit var rvAgenda: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agenda_fisio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvAgenda = findViewById(R.id.rvAgenda)
        rvAgenda.layoutManager = LinearLayoutManager(this)

        val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", Context.MODE_PRIVATE)
        val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)

        if (tokenGuardado != null) {
            tokenConBearer = "Bearer $tokenGuardado"
            cargarAgenda() // Pedimos la lista al arrancar
        }

    } // on Create

    private fun cargarAgenda() {
        RetrofitClient.apiService.obtenerTodasLasCitas(tokenConBearer).enqueue(object : retrofit2.Callback<List<Cita>> {
            override fun onResponse(call: retrofit2.Call<List<Cita>>, response: retrofit2.Response<List<Cita>>) {
                if (response.isSuccessful) {
                    val listaCompleta = response.body() ?: emptyList()

                    // ¡Aquí arreglamos la línea roja enseñándole el walkie-talkie!
                    val adaptador = CitaFisioAdapter(listaCompleta) { citaPulsada ->
                        mostrarVentanitaOpciones(citaPulsada)
                    }
                    rvAgenda.adapter = adaptador
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Cita>>, t: Throwable) {
                Toast.makeText(this@AgendaFisioActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarVentanitaOpciones(cita: Cita) {
        val opciones = arrayOf("Aceptar Cita", "Cancelar Cita")

        val ventanita = AlertDialog.Builder(this)
        ventanita.setTitle("Opciones de Cita")

        ventanita.setItems(opciones) { _, posicion ->
            when (posicion) {
                0 -> cambiarEstado(cita.id!!, "Aceptada")
                1 -> cambiarEstado(cita.id!!, "Cancelada")
            }
        }

        ventanita.setNegativeButton("Cerrar", null)
        ventanita.show()
    }

    private fun cambiarEstado(idCita: Long, nuevoEstado: String) {
        RetrofitClient.apiService.actualizarEstadoCita(tokenConBearer, idCita, nuevoEstado).enqueue(object : retrofit2.Callback<Cita> {
            override fun onResponse(call: retrofit2.Call<Cita>, response: retrofit2.Response<Cita>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AgendaFisioActivity, "Cita $nuevoEstado", Toast.LENGTH_SHORT).show()
                    cargarAgenda() // Recarga la lista para ver el cambio
                } else {
                    Toast.makeText(this@AgendaFisioActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: retrofit2.Call<Cita>, t: Throwable) {
                Toast.makeText(this@AgendaFisioActivity, "Fallo de red", Toast.LENGTH_SHORT).show()
            }
        })
    }




}
