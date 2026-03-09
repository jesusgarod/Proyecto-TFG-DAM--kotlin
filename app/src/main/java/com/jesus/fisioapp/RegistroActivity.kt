package com.jesus.fisioapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etNombre = findViewById<EditText>(R.id.etRegistroNombre)
        val etApellidos = findViewById<EditText>(R.id.etRegistroApellidos)
        val etEmail = findViewById<EditText>(R.id.etRegistroEmail)
        val etTelefono = findViewById<EditText>(R.id.etRegistroTelefono)
        val etPassword = findViewById<EditText>(R.id.etRegistroPassword)
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            val nombreEscrito = etNombre.text.toString()
            val apellidosEscritos = etApellidos.text.toString()
            val emailEscrito = etEmail.text.toString()
            val telefonoEscrito = etTelefono.text.toString()
            val passwordEscrita = etPassword.text.toString()

            if (nombreEscrito.isEmpty() || apellidosEscritos.isEmpty() || emailEscrito.isEmpty() || passwordEscrita.isEmpty() ||  telefonoEscrito.isEmpty()){

                Toast.makeText(this, "Por favor, rellena los campos obligatorios", Toast.LENGTH_SHORT).show()

            }else{

                val nuevoPaciente = RegistroRequest(nombreEscrito, apellidosEscritos,emailEscrito,
                    telefonoEscrito,passwordEscrita)

                RetrofitClient.apiService.registrarPaciente(nuevoPaciente).enqueue(object : retrofit2.Callback<Void> {

                    override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                        if (response.isSuccessful) {
                            // El servidor ha guardado al paciente
                            Toast.makeText(this@RegistroActivity, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show()

                            // Volvemos a la pantalla de Login para que pueda entrar con su nueva cuenta
                            finish()
                        } else {
                            // Probablemente el email ya exista en la base de datos
                            Toast.makeText(this@RegistroActivity, "Error al registrar. ¿Email ya en uso?", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                        Toast.makeText(this@RegistroActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }





        }
    }//onCreate
}
