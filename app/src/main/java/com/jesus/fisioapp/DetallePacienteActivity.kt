package com.jesus.fisioapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallePacienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvNombreDetalle = findViewById<TextView>(R.id.tvNombreDetalle)
        val btnAsignarEjercicio = findViewById<Button>(R.id.btnAsignarEjercicio)

        val idPaciente = intent.getLongExtra("ID_PACIENTE_ELEGIDO", -1L)
        val nombrePaciente = intent.getStringExtra("NOMBRE_PACIENTE_ELEGIDO") ?: "Paciente Desconocido"
        val apellidosPaciente = intent.getStringExtra("APELLIDOS_PACIENTE_ELEGIDO")

        val nombreCompleto = if (apellidosPaciente != null){
            "$nombrePaciente $apellidosPaciente"
        } else{
            nombrePaciente //si no tiene apellidos, se deja solo el nombre
        }
        tvNombreDetalle.text = nombreCompleto

        btnAsignarEjercicio.setOnClickListener {


        }

    }
}