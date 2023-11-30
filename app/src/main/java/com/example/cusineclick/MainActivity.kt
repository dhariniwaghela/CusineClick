package com.example.cusineclick

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cusineclick.databinding.ActivityMainBinding
import com.example.cusineclick.fragment.BottomNotificationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
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

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance()
        val userdatareference = database.reference.child("User").child("UserData").child(userId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val address = dataSnapshot.child("location").getValue(String::class.java)
                    binding.tvLocation.text = address

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(ContentValues.TAG, databaseError.getMessage()) //Don't ignore errors!
            }
        }
        userdatareference.addListenerForSingleValueEvent(valueEventListener)


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