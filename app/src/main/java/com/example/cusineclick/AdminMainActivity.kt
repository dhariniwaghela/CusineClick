package com.example.cusineclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cusineclick.databinding.ActivityAdminMainBinding
import com.example.cusineclick.databinding.ActivitySignUpAdminBinding

class AdminMainActivity : AppCompatActivity() {

    private val binding: ActivityAdminMainBinding by lazy {
        ActivityAdminMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addMenu.setOnClickListener {
            val mainintent = Intent(this, AddItemActivity::class.java)
            startActivity(mainintent)
        }
    }
}