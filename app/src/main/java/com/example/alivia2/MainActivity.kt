package com.example.alivia2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivityDebug", "onCreate CALLED")

        setContentView(R.layout.activity_main)
        Log.d("MainActivityDebug", "setContentView CALLED")

        Log.d("MainActivityDebug", "Handler about to be posted")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("MainActivityDebug", "Handler EXECUTING, starting DashboardActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Cambiado a 2 segundos
    }
}
