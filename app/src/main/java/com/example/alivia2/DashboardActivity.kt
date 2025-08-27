package com.example.alivia2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val user = FirebaseAuth.getInstance().currentUser
        val welcomeText = findViewById<TextView>(R.id.tvWelcome)
        // welcomeText.text = "¡Hola, ${user?.displayName ?: "Usuario"}!"
        val userName = user?.displayName ?: getString(R.string.default_user_name)
        welcomeText.text = getString(R.string.welcome_message, userName)
    }
}