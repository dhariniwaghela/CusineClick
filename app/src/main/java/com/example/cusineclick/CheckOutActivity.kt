package com.example.cusineclick

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.Fragment.ConfirmOrderBottomSheetFragment
import com.example.cusineclick.databinding.ActivityCheckOutBinding
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat


class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo: UserModel
    private lateinit var uid: String
    private lateinit var database:DatabaseReference
    var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance();
        uid = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("UserData")
        if (uid.isNotEmpty()) {
            //fetching user info
            getUserData()
        }

        val totalAmount = intent.getDoubleExtra("total", 0.0)
        binding.tvAmount.text = "CA$ $totalAmount"
        val df = DecimalFormat("##.##")

        val res: Double = totalAmount / 100.0 * 5.25

        binding.tvTax.text = "CA$ ${df.format(res)}"
         total = totalAmount + res
        binding.tvTotal.text = "CA$${df.format(total)}"
        binding.btncheckout.setOnClickListener {
            transferdata()
            val bottomSheetFragment = ConfirmOrderBottomSheetFragment()
            bottomSheetFragment.dialog?.setCancelable(false)
            bottomSheetFragment.dialog?.setCanceledOnTouchOutside(false)
            bottomSheetFragment.show(supportFragmentManager, "Test")
        }

    }

    private fun transferdata() {
        val restaurantName = intent.getStringExtra("RestaurantName")
        val destinationRef = database.child("Order").child(uid).child(restaurantName.toString())
        val sourceRef= database.child("User").child("UserData").child(uid).child("Cart").child(
            restaurantName.toString()
        )
        sourceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        // Get the unique key
                        val key = childSnapshot.key

                        // Get the data from the source node
                        val data = childSnapshot.value

                        // Set the data in the destination node with the same key
                        destinationRef.child(key!!).setValue(data)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Data copied successfully for this key
                                    Log.d("copy","copy data to order node")
                                } else {
                                    // Handle the error
                                }
                            }
                    }
                    removeDataFromCart()
                    destinationRef.child("OrderAmount").setValue(total)
                } else {
                    // Source node is empty or does not exist
                }
            }
                    private fun removeDataFromCart() {
// Remove the cart item node and its data
                        sourceRef.removeValue()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Node and data deleted successfully

                                } else {
                                    // Handle the error
                                }
                            }
                    }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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