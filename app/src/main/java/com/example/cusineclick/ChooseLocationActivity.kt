package com.example.cusineclick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.cusineclick.databinding.ActivityChooseLocationBinding
import com.example.cusineclick.databinding.ActivityLoginBinding

class ChooseLocationActivity : AppCompatActivity() {
    private val binding: ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locationList = arrayOf("Barrie","Toronto","Ottawa","Hamilton","Brampton","Kitchener","Oshawa","London","Windsor","Vaughan","Mississaga")
        val listadapter=ArrayAdapter(this,android.R.layout.simple_list_item_2,locationList)
        val autoCompleteTextView= binding.locationlist
        autoCompleteTextView.setAdapter(listadapter)
    }
}