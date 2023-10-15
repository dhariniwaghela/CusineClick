package com.example.cusineclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cusineclick.databinding.ActivityLoginBinding
import com.example.cusineclick.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnSignup.setOnClickListener {
            val mainintent= Intent(this, ChooseLocationActivity::class.java)
            startActivity(mainintent)
        }
        binding.tvlogin.setOnClickListener {
            val loginintent= Intent(this, LoginActivity::class.java)
            startActivity(loginintent)
        }
    }
}