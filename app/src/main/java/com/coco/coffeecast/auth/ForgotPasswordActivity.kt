package com.coco.coffeecast.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    // Declare variables for View Binding and Firebase Auth
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Set the click listener for the reset button
        binding.resetBtn.setOnClickListener {
            sendPasswordResetLink()
        }

        // Set the click listener for the "Back to Login" text
        binding.backToLogin.setOnClickListener {
            // Simply finish the activity to go back to the previous screen
            finish()
        }
    }

    private fun sendPasswordResetLink() {
        val email = binding.email.text.toString().trim()

        // Validate that the email field is not empty
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your registered email", Toast.LENGTH_SHORT).show()
            return
        }

        // Use Firebase to send the password reset email
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // On success, notify the user and close the screen
                    Toast.makeText(this, "Password reset link has been sent to your email.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    // On failure, show an error message
                    Toast.makeText(this, "Failed to send reset link: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}