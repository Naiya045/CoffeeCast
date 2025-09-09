package com.coco.coffeecast.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Set a personalized greeting message
        if (currentUser != null) {
            // Use the user's name if available, otherwise default to "Hello!"
            val displayName = currentUser.displayName
            if (!displayName.isNullOrEmpty()) {
                binding.greetingText.text = "Hello, ${displayName.split(" ")[0]}!"
            } else {
                binding.greetingText.text = "Hello!"
            }
        }

        // Handle clicks on new UI elements
        binding.orderCard.setOnClickListener {
            Toast.makeText(this, "Order Now clicked!", Toast.LENGTH_SHORT).show()
        }

        binding.featuredCard.setOnClickListener {
            Toast.makeText(this, "Deal of the Day clicked!", Toast.LENGTH_SHORT).show()
        }

        // Handle Logout
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}