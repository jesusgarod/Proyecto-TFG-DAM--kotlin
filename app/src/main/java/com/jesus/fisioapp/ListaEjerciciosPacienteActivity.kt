package com.jesus.fisioapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaEjerciciosPacienteActivity : AppCompatActivity() {

    private var tokenConBearer: String =""
    private var idPacienteElegido: Long = -1L
    private lateinit var rvEjerciciosPaciente: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_ejercicios_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvEjerciciosPaciente = findViewById(R.id.rvEjerciciosPaciente)
        rvEjerciciosPaciente.layoutManager = LinearLayoutManager(this)

        idPacienteElegido = intent.getLongExtra("ID_PACIENTE_DESTINO", -1L)
        val blocDeNotas  = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
        val tokenGuardaddo = blocDeNotas.getString("TOKEN_VIP", null)

        if (tokenGuardaddo != null && idPacienteElegido != -1L){

            tokenConBearer = "Bearer $tokenGuardaddo"
            cargarEjercicios()

        } else {

            Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
        }





    }

    private fun cargarEjercicios() {

        RetrofitClient.apiService.obtenerEjerciciosPaciente(tokenConBearer, idPacienteElegido)
            .enqueue(object : retrofit2.Callback<List<Ejercicio>> {

                override fun onResponse(call: retrofit2.Call<List<Ejercicio>>, response: retrofit2.Response<List<Ejercicio>>) {
                    if (response.isSuccessful) {
                        val listaReal = response.body() ?: emptyList()

                        // Contratamos al trabajador y le enseñamos qué hacer si hay un clic largo
                        val adaptador = EjercicioFisioAdapter(listaReal) { ejercicioPulsado ->
                            mostrarAvisoDeBorrado(ejercicioPulsado)
                        }
                        rvEjerciciosPaciente.adapter = adaptador

                    } else {
                        Toast.makeText(this@ListaEjerciciosPacienteActivity, "No se pudieron cargar los ejercicios", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Ejercicio>>, t: Throwable) {
                    Toast.makeText(this@ListaEjerciciosPacienteActivity, "Error de conexión", Toast.LENGTH_LONG).show()
                }
            })


    }

    private fun mostrarAvisoDeBorrado(ejercicio: Ejercicio) {
        val ventanita = AlertDialog.Builder(this)
        ventanita.setTitle("Borrar Ejercicio")
        ventanita.setMessage("¿Estás seguro de que quieres quitarle el ejercicio '${ejercicio.nombre}' a este paciente?")

        // Si dice que SÍ
        ventanita.setPositiveButton("Sí, borrar") { _, _ ->

            // Llamamos al cartero para que active la trituradora
            RetrofitClient.apiService.eliminarEjercicio(tokenConBearer, ejercicio.id!!)
                .enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(
                        call: retrofit2.Call<Void>,
                        response: retrofit2.Response<Void>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ListaEjerciciosPacienteActivity,
                                "Ejercicio borrado",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Volvemos a pedir la lista para que el ejercicio desaparezca de la pantalla
                            cargarEjercicios()
                        } else {
                            Toast.makeText(
                                this@ListaEjerciciosPacienteActivity,
                                "Error al borrar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                        Toast.makeText(
                            this@ListaEjerciciosPacienteActivity,
                            "Error de red",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }


        ventanita.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        ventanita.show()


    }


}