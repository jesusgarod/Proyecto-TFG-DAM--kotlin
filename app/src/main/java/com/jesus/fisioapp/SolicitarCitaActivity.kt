package com.jesus.fisioapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SolicitarCitaActivity : AppCompatActivity() {
    private var fechaElegida: String = ""
    private var horaElegida: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_solicitar_cita)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnElegirFecha = findViewById<Button>(R.id.btnElegirFecha)
        val btnElegirHora = findViewById<Button>(R.id.btnElegirHora)
        val btnConfirmarCita = findViewById<Button>(R.id.bntConfirmarCita)
        val etMotivoCita = findViewById<EditText>(R.id.etMotivoCita)

        //logica para el calendario
        btnElegirFecha.setOnClickListener {
            val calendario = Calendar.getInstance() //se coge la fecha de hoy
            val anioActual = calendario.get(Calendar.YEAR)
            val mesActual = calendario.get(Calendar.MONTH)
            val diaActual = calendario.get(Calendar.DAY_OF_MONTH)

            //crear la ventana del calendario
            val selectorFecha = DatePickerDialog(this, { _, anioElegido, mesElegido, diaElegido ->
                // los meses empiezan en 0 (Enero es 0), así que sumamos 1
                val mesReal = mesElegido + 1

                // Guardamos la fecha para luego y cambiamos el texto del botón
                fechaElegida = "$anioElegido-$mesReal-$diaElegido"
                btnElegirFecha.text = "Día: $diaElegido/$mesReal/$anioElegido"
            }, anioActual, mesActual, diaActual)

            selectorFecha.show() //hace que la ventana aparezva
        }

        //logica para el reloj
        btnElegirHora.setOnClickListener {
            val calendario = Calendar.getInstance()
            val horaActual = calendario.get(Calendar.HOUR_OF_DAY)
            val minutoActual = calendario.get(Calendar.MINUTE)

            val selectorHora = TimePickerDialog(this, { _, horaSeleccionada, minutoSeleccionado ->

               val formatoHora = horaSeleccionada.toString().padStart(2,'0')
                val formatoMinuto = minutoSeleccionado.toString().padStart(2,'0')

                horaElegida = "$formatoHora:$formatoMinuto"
                btnElegirHora.text = "Hora: $horaElegida"
            }, horaActual, minutoActual, true) // El "true" es para usar formato 24 horas

            selectorHora.show()
        }

        btnConfirmarCita.setOnClickListener {

            val motivo = etMotivoCita.text.toString()

            if (fechaElegida.isEmpty() || horaElegida.isEmpty() || motivo.isEmpty()){

                Toast.makeText(this, "Por favor, elige el día, la hora y el motivo de la consulta.",
                    Toast.LENGTH_SHORT).show()
            }else {

                val ventanita = AlertDialog.Builder(this)
                ventanita.setTitle("Confirmar Cita")
                ventanita.setMessage("Quieres solicitar la cita para el día $fechaElegida a las $horaElegida ?")

                ventanita.setPositiveButton("Si, Confirmar")
                { _, _ ->

                    // Empaquetamos los datos que ha seleccionado el paciente
                    val nuevaCita = Cita(null,fechaElegida, horaElegida, motivo)

                    // 2. Recuperamos las llaves del bloc de notas
                    val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
                    val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)
                    val idDelPaciente = blocDeNotas.getLong("ID_PACIENTE", 1L)

                    if (tokenGuardado != null) {
                        val tokenConBearer = "Bearer $tokenGuardado"

                        // se envia el paquete al servidor
                        RetrofitClient.apiService.solicitarCita(
                            tokenConBearer, idDelPaciente, nuevaCita
                        ).enqueue(object : retrofit2.Callback<Cita> {

                            override fun onResponse(
                                call: retrofit2.Call<Cita>,
                                response: retrofit2.Response<Cita>
                            ) {
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        this@SolicitarCitaActivity,
                                        "¡Cita solicitada con éxito!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish() // Esto cierra la pantalla y nos devuelve al menú principal automáticamente
                                } else {
                                    Toast.makeText(
                                        this@SolicitarCitaActivity,
                                        "Error al guardar la cita en el servidor",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<Cita>, t: Throwable) {
                                Toast.makeText(
                                    this@SolicitarCitaActivity,
                                    "Fallo de conexión: ${t.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    }
                }
                ventanita.setNegativeButton("Cancelar", null)
                ventanita.show()
            }

        }
    }


}


