package com.example.coffeecast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class forgot_password : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailInput = findViewById<EditText>(R.id.email)
        val resetBtn = findViewById<Button>(R.id.reset_btn)
        val backToLogin = findViewById<TextView>(R.id.back_to_login)

        // Reset Password click
        resetBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Add real reset logic (API / Firebase)
                Toast.makeText(this, "Reset link sent to $email", Toast.LENGTH_LONG).show()
            }
        }

        // Back to login
        backToLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}
