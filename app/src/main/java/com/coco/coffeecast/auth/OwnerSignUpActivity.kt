package com.coco.coffeecast.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityOwnerSignUpBinding
import com.coco.coffeecast.main.OwnerMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OwnerSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnerSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.signupBtn.setOnClickListener {
            registerOwner()
        }

        binding.loginRedirectText.setOnClickListener {
            // Finish this activity to go back to the Owner Login screen
            finish()
        }
    }

    private fun registerOwner() {
        // Get all values from the EditText fields
        val businessName = binding.businessName.text.toString().trim()
        val ownerName = binding.ownerName.text.toString().trim()
        val mobile = binding.mobileNumber.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()

        // Validate that no fields are empty
        if (businessName.isEmpty() || ownerName.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate that passwords match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // If auth user is created, save their details to Firestore
                        saveOwnerDetails(userId, businessName, ownerName, mobile, email)
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveOwnerDetails(userId: String, businessName: String, ownerName: String, mobile: String, email: String) {
        val ownerData = hashMapOf(
            "businessName" to businessName,
            "ownerName" to ownerName,
            "mobile" to mobile,
            "email" to email,
            "uid" to userId
        )

        // Save the data in the 'owners' collection with the document ID matching the user's UID
        db.collection("owners").document(userId).set(ownerData)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                // Navigate to the owner's main dashboard
                val intent = Intent(this, OwnerMainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save details: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}