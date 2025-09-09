package com.coco.coffeecast.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coco.coffeecast.auth.OwnerLoginActivity
import com.coco.coffeecast.auth.UserLoginActivity
import com.coco.coffeecast.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCustomer.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
        }

        binding.btnOwner.setOnClickListener {
            // CHANGED: This now goes to the Owner LOGIN screen first.
            startActivity(Intent(this, OwnerLoginActivity::class.java))
        }
    }
}