package com.example.cusineclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cusineclick.databinding.ActivityMainBinding
import com.example.cusineclick.fragment.BottomNotificationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.fragmentContainerView)
        var bottomNav= findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNav.setupWithNavController(navController)

        binding.notificationButton.setOnClickListener{
            val bottomSheetDialog = BottomNotificationFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")

        }

        binding.tvLocation.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,ChooseLocationActivity::class.java)
            startActivityForResult(intent,555)
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==555){
            var locationString = data?.getStringExtra("location")
            Log.d("loc", locationString.toString())
            binding.tvLocation.text = locationString
        }
    }
}