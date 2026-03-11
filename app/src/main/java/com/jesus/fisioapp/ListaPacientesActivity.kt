package com.jesus.fisioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaPacientesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Buscamos el tablón en el diseño
        val rvPacientes = findViewById<RecyclerView>(R.id.rvPacientes)
        rvPacientes.layoutManager = LinearLayoutManager(this)

        // 2. Recuperamos la llave del fisio del bloc de notas
        val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
        val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)

        if (tokenGuardado != null) {
            val tokenConBearer = "Bearer $tokenGuardado"

            // 3. Llamamos al servidor pidiendo la lista de todos los usuarios
            RetrofitClient.apiService.obtenerTodosLosPacientes(tokenConBearer).enqueue(object : retrofit2.Callback<List<Paciente>> {

                override fun onResponse(call: retrofit2.Call<List<Paciente>>, response: retrofit2.Response<List<Paciente>>) {
                    if (response.isSuccessful) {
                        val listaReal = response.body()

                        if (listaReal != null) {

                            val soloPacientes = listaReal.filter { paciente -> paciente.rol?.uppercase() != "FISIO" }
                            val adaptador = PacienteAdapter(soloPacientes) { pacientePulsado ->

                                // Cuando toquen a un paciente, preparamos el viaje
                                val viaje = Intent(this@ListaPacientesActivity, DetallePacienteActivity::class.java)

                                // Metemos en la maleta los datos importantes para usarlos en la siguiente pantalla
                                viaje.putExtra("ID_PACIENTE_ELEGIDO", pacientePulsado.id)
                                viaje.putExtra("NOMBRE_PACIENTE_ELEGIDO", pacientePulsado.nombre)
                                viaje.putExtra("APELLIDOS_PACIENTE_ELEGIDO", pacientePulsado.apellidos)

                                startActivity(viaje)
                            }
                            rvPacientes.adapter = adaptador
                        }
                    } else {
                        Toast.makeText(this@ListaPacientesActivity, "Error al cargar los pacientes", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Paciente>>, t: Throwable) {
                    Toast.makeText(this@ListaPacientesActivity, "Fallo de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}

