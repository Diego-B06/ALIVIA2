package com.example.alivia2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usar el layout correcto para el Dashboard
        setContentView(R.layout.activity_dashboard)

        val user = FirebaseAuth.getInstance().currentUser
        // Usar los IDs correctos definidos en activity_dashboard.xml
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        val userName = user?.displayName ?: getString(R.string.default_user_name)
        welcomeText.text = getString(R.string.welcome_message, userName)

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Cerrar sesión en Firebase
            val intent = Intent(this, LoginActivity::class.java)
            // Limpiar el stack de actividades para que el usuario no pueda volver al dashboard con el botón "atrás"
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Cerrar DashboardActivity
        }
    }
}