package com.coco.coffeecast.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() { // Renamed class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // If a user is logged in, you might want to check if they are an owner or customer
                // For now, we'll send them to the main customer activity
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // No user is logged in, go to WelcomeActivity
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            finish()
        }, 2000)
    }
}