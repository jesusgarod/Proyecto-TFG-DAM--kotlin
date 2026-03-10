package com.jesus.fisioapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.Call
import retrofit2.Response

class MenuPrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // buscamos por su ID
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        val tvPerfinlEdad = findViewById<TextView>(R.id.tvPerfilEdad)
        val tvPerfilTelefono = findViewById<TextView>(R.id.tvPerfilTelefono)
        val btnEjercicios = findViewById<Button>(R.id.btnEjercicios)

        val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
        val tokenGuardado = blocDeNotas.getString("TOKEN_VIP", null)

        if (tokenGuardado != null){

            val tokenConBearer = "Bearer $tokenGuardado"

            RetrofitClient.apiService.obtenerPerfilUsuario(tokenConBearer).enqueue(object : retrofit2.Callback<UsuarioResponse> {

                override fun onResponse(call: retrofit2.Call<UsuarioResponse>, response: retrofit2.Response<UsuarioResponse>) {
                    if (response.isSuccessful) {
                        // si tenemos los datos del paciente
                        val paciente = response.body()
//                        Toast.makeText(this@MenuPrincipalActivity, "¡Bienvenido a tu clínica, ${paciente?.nombre}!", Toast.LENGTH_LONG).show()
                        if (paciente != null){

                            tvBienvenida.text = "¡Hola, ${paciente.nombre} ${paciente.apellidos} !"
                            tvPerfinlEdad.text = "Edad: ${paciente.edad}"
                            tvPerfilTelefono.text = "Teléfono: ${paciente.telefono}"

                            val editor = blocDeNotas.edit()
                            editor.putLong("ID_PACIENTE", paciente.id)
                            editor.apply()
                        }
                    } else {
                        Toast.makeText(this@MenuPrincipalActivity, "Error de seguridad: Tu sesión ha caducado", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<UsuarioResponse>, t: Throwable) {
                    Toast.makeText(this@MenuPrincipalActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            // Si por algún motivo llegamos aquí y no hay token, lo ideal sería devolverlo al Login
            Toast.makeText(this, "No hay sesión iniciada", Toast.LENGTH_SHORT).show()
        }

        btnCerrarSesion.setOnClickListener {

            val editor = blocDeNotas.edit()
            editor.clear() //borra lo que este guardado
            editor.apply() // se confirma el borrado anterior

            Toast.makeText(this@MenuPrincipalActivity, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()

            val vueltaAlLogin = Intent(this@MenuPrincipalActivity, MainActivity::class.java)
            startActivity(vueltaAlLogin)

            finish()


        }

        btnEjercicios.setOnClickListener {

            val misEjercicios = Intent (this@MenuPrincipalActivity, EjerciciosActivity::class.java)
            startActivity(misEjercicios)
        }







    }
}