package com.example.cusineclick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityFoodDetailsBinding

class FoodDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFoodDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var foodname = intent.getStringExtra("MenuItemName")
        val foodImage = intent.getIntExtra("MenuItemImage",0)
        binding.tvFoodname.text = foodname
        binding.foodImage.setImageResource(foodImage)

        binding.buttonBack.setOnClickListener{
         finish()
        }



    }
}