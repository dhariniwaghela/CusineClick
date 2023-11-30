package com.example.cusineclick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityCheckOutBinding
import com.example.cusineclick.fragment.ConfirmOrderBottomSheetFragment
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.checkerframework.checker.nullness.qual.NonNull
import java.text.DecimalFormat


class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo: UserModel
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance();
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("UserData")
        if (uid.isNotEmpty()) {
            //fetching user info
            getUserData()
        }

        val totalAmount = intent.getDoubleExtra("total",0.0)
        binding.tvAmount.text = "CA$ $totalAmount"

        val df = DecimalFormat("0.00")
        val res: Double = totalAmount / 100.0f * 5.25
        binding.tvTax.text = "CA$ ${df.format(res)}"
        val total:Double = totalAmount + res
        binding.tvTotal.text = "CA$${df.format(total)}"
        binding.btncheckout.setOnClickListener {
            val bottomSheetFragment = ConfirmOrderBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, "Test")
            transferdata()
        }
    }

    private fun transferdata() {
        val sourceRef = databaseReference.child(uid).child("CartItem")
        val destinationRef = databaseReference.child(uid).child("OrderHistory")

        sourceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the data from the source node
                val data = dataSnapshot.value
                // Write the data to the destination node
                destinationRef.setValue(data)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userinfo = snapshot.getValue(UserModel::class.java)!!
                binding.tvName.text = userinfo.name
                binding.tvAddress.text = userinfo.location
                binding.tvcity.text = userinfo.city
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}