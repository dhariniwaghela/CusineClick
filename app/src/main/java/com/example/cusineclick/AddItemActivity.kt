package com.example.cusineclick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cusineclick.databinding.ActivityAddItemBinding
import com.example.cusineclick.databinding.ActivityLoginAdminBinding

class AddItemActivity : AppCompatActivity() {

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.selectimage.setOnClickListener {
            pickimage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.backButton.setOnClickListener {
           finish()
        }
    }

    val pickimage=registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            uri ->
        if(uri != null){
            binding.selectedimage.setImageURI(uri)
        }
    }
}