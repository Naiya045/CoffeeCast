package com.coco.coffeecast.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityOwnerLoginBinding
import com.coco.coffeecast.main.OwnerMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OwnerLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnerLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Handles the main login button
        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.pwd.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        checkIfUserIsOwner(task.result.user!!.uid)
                    } else {
                        Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // ADDED: This makes the "Sign Up" text clickable and functional
        binding.signupRedirectText.setOnClickListener {
            // Navigate to the Owner Sign Up screen
            startActivity(Intent(this, OwnerSignUpActivity::class.java))
        }
    }

    private fun checkIfUserIsOwner(uid: String) {
        db.collection("owners").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Owner Login Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OwnerMainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "This account is not a registered owner.", Toast.LENGTH_LONG).show()
                    auth.signOut()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error verifying owner status.", Toast.LENGTH_LONG).show()
                auth.signOut()
            }
    }
}