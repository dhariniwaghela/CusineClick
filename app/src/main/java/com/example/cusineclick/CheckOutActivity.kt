package com.example.cusineclick

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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


class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo: UserModel
    private lateinit var uid: String
    private var itemname: ArrayList<String> = ArrayList()
    private lateinit var itemPrice: ArrayList<String>
    private lateinit var itemCategory: ArrayList<String>
    private lateinit var itemCalories: ArrayList<String>
    private lateinit var itemImg: ArrayList<String>
    private lateinit var itemIngredient: ArrayList<String>
    private lateinit var itemQuantities: ArrayList<Int>

    private lateinit var totalAmount: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance();
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("user")
        if (uid.isNotEmpty()) {
            //fetching user info
            getUserData()
        }
        //get user cart details from firebase
       /*
        val intent = intent
        itemname = intent.getStringArrayExtra("ItemNames") as ArrayList<String>
        itemPrice = intent.getStringArrayExtra("ItemPrice") as ArrayList<String>
        itemCategory = intent.getStringArrayExtra("ItemCategory") as ArrayList<String>
        itemCalories = intent.getStringArrayExtra("ItemCalories") as ArrayList<String>
        itemIngredient = intent.getStringArrayExtra("ItemIngredient") as ArrayList<String>
        itemImg = intent.getStringArrayExtra("ItemImg") as ArrayList<String>
        itemQuantities = intent.getIntegerArrayListExtra("ItemQuantity") as ArrayList<Int>

        Log.d("items", "intent received")

        totalAmount = CalculateTotalAmount().toString() + "$"
        binding.tvAmount.isEnabled = false
        binding.tvAmount.text = totalAmount

*/
        binding.checkout.setOnClickListener {
            val bottomSheetFragment = ConfirmOrderBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, "Test")
        }

    }

  /*  private fun CalculateTotalAmount(): Int {
        var totalAmount = 0
            for (i in 0 until itemPrice!!.size) {
                var price = itemPrice!![i]
                val lastChar = price.last()
                val priceIntVal = if (lastChar == '$') {
                    price.dropLast(1).toInt()
                } else {
                    price.toInt()

                }
                var quantity = itemQuantities!![i].toInt()
                totalAmount += quantity * priceIntVal
            }
        return totalAmount
    }

   */

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userinfo = snapshot.getValue(UserModel::class.java)!!
                binding.tvName.text = userinfo.name
                binding.tvAddress.text = userinfo.location
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}