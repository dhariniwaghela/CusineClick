package com.example.cusineclick

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private val binding :ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnLogin.setOnClickListener {
            val loginintent = Intent(this, LoginActivity::class.java)
            startActivity(loginintent)
        }
        binding.btnSignup.setOnClickListener {
            val signupintent = Intent(this, SignUpActivity::class.java)
            startActivity(signupintent)
        }

    }

}