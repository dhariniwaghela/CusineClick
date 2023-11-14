package com.example.cusineclick

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.example.cusineclick.databinding.ActivitySignUpAdminBinding
import com.example.cusineclick.databinding.ActivitySignUpBinding

class SignUpAdminActivity : AppCompatActivity() {

    private val binding: ActivitySignUpAdminBinding by lazy {
        ActivitySignUpAdminBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.adminloginscreen.setOnClickListener {
            val loginintent = Intent(this, LoginAdminActivity::class.java)
            startActivity(loginintent)
        }
        binding.btnAdminSignup.setOnClickListener {
            val mainintent = Intent(this, AdminMainActivity::class.java)
            startActivity(mainintent)
        }
        val locationList = arrayOf("Jaipur","Gujarat","Maharashtra","Odisha","Punjab","Banglore")
        val adapter =  ArrayAdapter(this, R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)
     }
}