package com.example.cusineclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cusineclick.databinding.ActivityLoginBinding
import com.example.cusineclick.databinding.ActivityStartBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            val mainintent=Intent(this, ChooseLocationActivity::class.java)
            startActivity(mainintent)
        }
        binding.tvsignup.setOnClickListener {
            val signupintent=Intent(this,SignUpActivity::class.java)
            startActivity(signupintent)
        }
        binding.tvforgotpassword.setOnClickListener {
            val signupintent=Intent(this,ForgotPasswordActivity::class.java)
            startActivity(signupintent)
        }

    }
}
