package com.example.cusineclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cusineclick.databinding.ActivityForgotPasswordBinding
import com.example.cusineclick.databinding.ActivityLoginBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private val binding: ActivityForgotPasswordBinding by lazy{
        ActivityForgotPasswordBinding.inflate(layoutInflater )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnsave.setOnClickListener {
            val mainintent=Intent(this, StartActivity::class.java)
            startActivity(mainintent)
        }    }
}