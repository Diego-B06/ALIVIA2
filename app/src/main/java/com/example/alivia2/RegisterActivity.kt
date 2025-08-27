package com.example.alivia2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        val etEmailRegister = findViewById<TextInputEditText>(R.id.etEmailRegister)
        val etPasswordRegister = findViewById<TextInputEditText>(R.id.etPasswordRegister)
        val etConfirmPasswordRegister = findViewById<TextInputEditText>(R.id.etConfirmPasswordRegister)
        val btnRegisterNewAccount = findViewById<Button>(R.id.btnRegisterNewAccount)
        val tvLoginLink = findViewById<TextView>(R.id.tvLoginLink)

        btnRegisterNewAccount.setOnClickListener {
            val email = etEmailRegister.text.toString().trim()
            val password = etPasswordRegister.text.toString().trim()
            val confirmPassword = etConfirmPasswordRegister.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Añadir validación de fortaleza de contraseña si se desea (e.g., longitud mínima)

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("RegisterActivity", "createUserWithEmail:success")
                        Toast.makeText(baseContext, "Cuenta creada exitosamente.", Toast.LENGTH_SHORT).show()
                        // Opcional: Iniciar sesión automáticamente y navegar al Dashboard
                        // val user = mAuth.currentUser
                        // updateUI(user) // Necesitarías una función updateUI o navegar directamente
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Error al crear cuenta: ${task.exception?.message}",
                            Toast.LENGTH_LONG).show()
                        // updateUI(null)
                    }
                }
        }

        tvLoginLink.setOnClickListener {
            // Volver a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Opcional: cierra RegisterActivity para que no quede en el stack
        }
    }
}
