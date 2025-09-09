package com.coco.coffeecast.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityMainOwnerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OwnerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainOwnerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainOwnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Set the owner's name in the greeting
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // We can get the owner's name from our 'owners' collection
            db.collection("owners").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val ownerName = document.getString("ownerName")
                        binding.ownerNameText.text = "Welcome, $ownerName!"
                    }
                }
        }

        // Set up click listeners for the dashboard buttons
        binding.manageOrdersBtn.setOnClickListener {
            Toast.makeText(this, "Manage Orders Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.editMenuBtn.setOnClickListener {
            Toast.makeText(this, "Edit Menu Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}