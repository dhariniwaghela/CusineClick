package com.example.cusineclick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityCheckOutBinding
import com.example.cusineclick.fragment.ConfirmOrderBottomSheetFragment


class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
       binding.checkout.setOnClickListener{
         val bottomSheetFragment = ConfirmOrderBottomSheetFragment()
           bottomSheetFragment.show(supportFragmentManager,"Test")
       }

    }
}