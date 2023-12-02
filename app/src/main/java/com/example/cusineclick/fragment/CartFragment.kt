package com.example.cusineclick.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.CheckOutActivity
import com.example.cusineclick.adapter.CartAdapter
import com.example.cusineclick.databinding.FragmentCartBinding
import com.example.cusineclick.model.CartItem
import com.example.cusineclick.model.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private lateinit var cartItems: MutableList<CartItem>
    private lateinit var cartAdapter: CartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCartBinding.inflate(inflater, container, false)

        //initialize firebase
        auth = FirebaseAuth.getInstance()
        //database reference to firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        cartItems = mutableListOf()

        setAdapter()

        retriveCartItems()

        binding.btnProceed.setOnClickListener(View.OnClickListener {
            //get order items details before procceeding to check out

            var total = getOrderItemDetails()
            val intent = Intent(requireContext(), CheckOutActivity::class.java)
            intent.putExtra("total", total)
            startActivity(intent)

        })
        return binding.root
    }

    private fun getOrderItemDetails(): Double {
        var total = 0.0
        if (cartItems.size > 0) {
            for (cartitem in cartItems) {
                var price = cartitem.foodItemPrice?.toDouble()
                var itemQty = cartitem.foodItemQuantity?.toDouble()
                if (price != null) {
                    var itemIndPrice = itemQty?.times(price);
                    if (itemIndPrice != null) {
                        total += itemIndPrice
                    }
                }
            }
        }
        return total
        Log.d("total", total.toString())
    }


    private fun retriveCartItems() {
        //database reference to firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val cartItemRef: DatabaseReference = database.reference.child("User").child("UserData").child(userId).child("CartItems")
        cartItems = mutableListOf()
        cartItemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    val cartItem = foodsnapshot.getValue(CartItem::class.java)
                    cartItem?.let {
                        cartItems.add(it)
                    }
                    cartAdapter.updateList(cartItems)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(
            requireContext()
        )
        binding.cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartRecyclerView.adapter = cartAdapter

    }

    companion object {

    }
}

