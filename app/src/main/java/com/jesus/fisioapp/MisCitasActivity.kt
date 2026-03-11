package com.jesus.fisioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MisCitasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mis_citas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val rvCitas = findViewById<RecyclerView>(R.id.rvCitas)
        val btnIrAPedirCita = findViewById<Button>(R.id.btnIrAPedirCita)

        rvCitas.layoutManager = LinearLayoutManager(this)

        btnIrAPedirCita.setOnClickListener {

            val irASolicitar = Intent(this, SolicitarCitaActivity::class.java)
            startActivity(irASolicitar)
        }
    }

    //para que se actualize automaticamente la pantalla con cambios
    override fun onResume(){

        super.onResume()

        val rvCitas = findViewById<RecyclerView>(R.id.rvCitas)
        val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
        val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)
        val idDelPaciente = blocDeNotas.getLong("ID_PACIENTE", 1L)

        if (tokenGuardado != null) {
            val tokenConBearer = "Bearer $tokenGuardado"

            // Llamamos al servidor pidiendo la lista de citas
            RetrofitClient.apiService.obtenerMisCitas(tokenConBearer, idDelPaciente)
                .enqueue(object : retrofit2.Callback<List<Cita>> {

                    override fun onResponse(call: retrofit2.Call<List<Cita>>,response: retrofit2.Response<List<Cita>>
                    ) {
                        if (response.isSuccessful) {
                            val listaReal = response.body()

                            if (listaReal != null) {

                                val adaptador = CitaAdapter(listaReal) { idCitaParaBorrar ->

                                    // Preparamos la ventanita para evitar borrados por accidente
                                    val ventanita = android.app.AlertDialog.Builder(this@MisCitasActivity)
                                    ventanita.setTitle("Cancelar cita")
                                    ventanita.setMessage("¿Estás seguro de que quieres anular esta cita?")

                                    ventanita.setPositiveButton("Sí, anular") { _, _ ->
                                        // Si dice que sí, llamamos a la función de borrar
                                        borrarCita(idCitaParaBorrar)
                                    }

                                    ventanita.setNegativeButton("No", null)
                                    ventanita.show()

                                }

                                rvCitas.adapter = adaptador
                            }
                        } else {
                            Toast.makeText(this@MisCitasActivity,"Error al cargar las citas",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<List<Cita>>, t: Throwable) {
                        Toast.makeText(this@MisCitasActivity,"Fallo de conexión: ${t.message}",Toast.LENGTH_LONG).show()
                    }
                })
        }
    }


    private fun borrarCita(idCita: Long){
        val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
        val tokenGuardado = blocDeNotas.getString("TOKEN_VIP",null)

        if (tokenGuardado != null) {
            val tokenConBearer = "Bearer $tokenGuardado"

            RetrofitClient.apiService.eliminarCita(tokenConBearer, idCita).enqueue(object : retrofit2.Callback<Void> {
                override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MisCitasActivity, "Cita cancelada", Toast.LENGTH_SHORT).show()
                        onResume() //llama a onResume para actualizar el view
                    }
                }
                override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                    Toast.makeText(this@MisCitasActivity, "Error al borrar: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
}