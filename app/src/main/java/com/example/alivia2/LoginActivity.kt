package com.example.alivia2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Asegúrate que este es el layout que renombraste

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        // val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        // val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Aquí iría la lógica de autenticación con Firebase
                Toast.makeText(this, "Login Intento: Email: $email", Toast.LENGTH_LONG).show()
                // Si el login es exitoso, navegarías a DashboardActivity:
                // val intent = Intent(this, DashboardActivity::class.java)
                // startActivity(intent)
                // finish() // Opcional, para cerrar LoginActivity
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        // Puedes añadir listeners para tvForgotPassword y tvRegister si es necesario
    }
}