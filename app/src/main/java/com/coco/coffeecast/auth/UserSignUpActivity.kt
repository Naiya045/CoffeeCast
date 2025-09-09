package com.coco.coffeecast.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityUserSignUpBinding
import com.coco.coffeecast.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    // REMOVED: All Google Sign-Up variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Handle the Email/Password Sign Up button
        binding.signupBtn.setOnClickListener {
            registerUser()
        }

        // REMOVED: Click listener for Google Sign Up button

        // Handle the "Login" redirect link
        binding.loginRedirectText.setOnClickListener {
            finish()
        }
    }

    private fun registerUser() {
        val name = binding.name.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.pwd.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { saveUserInfo(it, name) }
                } else {
                    Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserInfo(user: FirebaseUser, name: String) {
        val userData = hashMapOf(
            "uid" to user.uid,
            "name" to name,
            "email" to user.email
        )

        db.collection("users").document(user.uid).set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save user details: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
    // REMOVED: All Google Sign-Up helper functions
}