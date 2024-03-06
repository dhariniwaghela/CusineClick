package com.example.cusineclick

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cusineclick.databinding.ActivityMainBinding
import com.example.cusineclick.Fragment.BottomNotificationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNav.setupWithNavController(navController)

        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = BottomNotificationFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")

        }

        binding.tvLocation.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ChooseLocationActivity::class.java)
            startActivityForResult(intent, 555)
        })

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance()
        val databaseReference = database.reference.child("User").child("UserData").child(userId)
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val address = snapshot.child("location").getValue(String::class.java)
                    if (address != null) {
                        binding.tvLocation.text = address
                    } else {
                        binding.tvLocation.text = ""
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(ContentValues.TAG, databaseError.getMessage()) //Don't ignore errors!
            }

        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check that it is the SecondActivity with an OK result
        if (requestCode == 555) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                // get String data from Intent
                val returnString = data?.getStringExtra("loc")
                // set text view with string
                binding.tvLocation.text = returnString
            }
        }
    }
}




