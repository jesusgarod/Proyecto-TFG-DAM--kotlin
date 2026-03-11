package com.jesus.fisioapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Buscan los elementos por el ID

        val cajaEmail = findViewById<EditText>(R.id.etEmail)
        val cajaPassword = findViewById<EditText>(R.id.etPassword)
        val botonEntrar = findViewById<Button>(R.id.btnLogin)
        val textoRegistro = findViewById<TextView>(R.id.tvIrRegistro)

        // listener para el boton
        botonEntrar.setOnClickListener {

            // extrae el mail y la pass del paciente
            val emailEscrito = cajaEmail.text.toString()
            val passwordEscrita = cajaPassword.text.toString()

            if (emailEscrito.isEmpty() || passwordEscrita.isEmpty()) {

                // si falta algun dato muestra un aviso
                Toast.makeText(this, "Faltan datos por rellenar", Toast.LENGTH_SHORT).show()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(emailEscrito).matches()) {

                //Se comprueba que el formato del mail sea el correcto
                Toast.makeText(this,"Por favor, introduce un correo válido", Toast.LENGTH_SHORT).show()
                cajaEmail.error = "Formato incorrecto" // aviso rojo si el formato no es correcto
            }
            else {

                // preparo los datos introducidos
                val peticion = LoginRequest(emailEscrito,passwordEscrita)

                RetrofitClient.apiService.iniciarSesion(peticion).enqueue(object : retrofit2.Callback<LoginResponse> {

                    // Si el servidor responde (sea con éxito o con error 403/404)
                    override fun onResponse(call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                        if (response.isSuccessful) {

                            val respuestaLogin = response.body()
                            val tokenRecibido = respuestaLogin?.token

                            // Extraemos el rol. Si por algún motivo falla, asumimos que es un paciente normal.
                            val rolRecibido = respuestaLogin?.rol ?: "PACIENTE"

                            if (tokenRecibido != null){
                                // SharedPreferences es un bloc de notas que tiene el móvil
                                val blocDeNotas = getSharedPreferences("MisPreferenciasFisioApp", MODE_PRIVATE)
                                val editor = blocDeNotas.edit()

                                // Se guarda el token
                                editor.putString("TOKEN_VIP", tokenRecibido)
                                editor.apply()
                                Toast.makeText(this@MainActivity,"El rol es: $rolRecibido", Toast.LENGTH_SHORT).show()

                                // --- EL CRUCE DE CAMINOS ---
                                if (rolRecibido.uppercase() == "FISIO") {
                                    // Puerta A: Es el jefe, va a su panel de control
                                    val saltoFisio = Intent(this@MainActivity, MenuFisioActivity::class.java)
                                    startActivity(saltoFisio)
                                } else {
                                    // Puerta B: Es un paciente, va al menú principal de siempre
                                    val saltoPaciente = Intent(this@MainActivity, MenuPrincipalActivity::class.java)
                                    startActivity(saltoPaciente)
                                }

                                finish()

                            }

                        } else {
                            // Semáforo rojo (Ej: Contraseña mal)
                            Toast.makeText(this@MainActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // Si el servidor está apagado o no hay internet
                    override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error al conectar: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })

            }





        }

        textoRegistro.setOnClickListener {
            val saltoRegistro = Intent(this, RegistroActivity::class.java)
            startActivity(saltoRegistro)
        }
    } // onCreate
}