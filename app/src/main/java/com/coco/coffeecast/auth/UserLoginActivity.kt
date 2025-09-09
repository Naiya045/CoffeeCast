package com.coco.coffeecast.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityUserLoginBinding
import com.coco.coffeecast.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    // REMOVED: All Google Sign-In variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Handle Email/Password Login
        binding.loginBtn.setOnClickListener {
            loginWithEmailPassword()
        }

        // REMOVED: Click listener for Google Login button

        // Handle "SignUp Now" Text
        binding.signupText.setOnClickListener {
            startActivity(Intent(this, UserSignUpActivity::class.java))
        }

        // Handle "Forgot Password?" Text
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun loginWithEmailPassword() {
        val email = binding.email.text.toString().trim()
        val password = binding.pwd.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    checkIfUserIsCustomer(task.result.user!!)
                } else {
                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun checkIfUserIsCustomer(user: FirebaseUser) {
        db.collection("owners").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Owner accounts cannot log in here.", Toast.LENGTH_LONG).show()
                    auth.signOut()
                } else {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error verifying user status.", Toast.LENGTH_LONG).show()
                auth.signOut()
            }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    // REMOVED: All Google Sign-In helper functions
}