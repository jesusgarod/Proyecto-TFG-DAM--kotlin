package com.jesus.fisioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.MainScope

class MenuFisioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_fisio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVerPacientes = findViewById<Button>(R.id.btnVerPacientes)
        val btnAgendaCitas = findViewById<Button>(R.id.btnAgendaCitas)
        val btnCerrarSesionFisio = findViewById<Button>(R.id.btnCerrarSesionFisio)

        btnVerPacientes.setOnClickListener {

            val irAPacientes = Intent(this@MenuFisioActivity, ListaPacientesActivity::class.java)
            startActivity(irAPacientes)

        }

        btnAgendaCitas.setOnClickListener {

            val irAgenda = Intent(this@MenuFisioActivity, AgendaFisioActivity::class.java)
            startActivity(irAgenda)

        }

        btnCerrarSesionFisio.setOnClickListener {

            //se coge el bloc y se borra el token y el id
            val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
            val editor = blocDeNotas.edit()
            editor.clear()
            editor.apply()

            val saltoLogin = Intent(this, MainActivity::class.java)
            startActivity(saltoLogin)

            finish()
        }


    }
}