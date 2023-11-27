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

    private lateinit var cartItemName: MutableList<String>
    private lateinit var cartItemPrice: MutableList<String>
    private lateinit var cartItemImageUri: MutableList<String>
    private lateinit var cartItemDesc: MutableList<String>
    private lateinit var cartItemIngredients: MutableList<String>
    private lateinit var cartItemQuantity: MutableList<Int>
    private lateinit var cartItemCategory: MutableList<String>
    private lateinit var cartItemCalories: MutableList<String>

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
        retriveCartItems()

        binding.btnProceed.setOnClickListener(View.OnClickListener {
            //get order items details before procceeding to check out
       //     getOrderItemDetails()
            val intent = Intent(requireContext(),CheckOutActivity::class.java)
            startActivity(intent)

        })
        return binding.root
    }

  /*  private fun getOrderItemDetails() {
        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")
        val orderfoodnames = mutableListOf<String>()
        val orderfoodPrice = mutableListOf<String>()
        val orderfoodImg = mutableListOf<String>()
        val orderfoodCategory = mutableListOf<String>()
        val orderfoodCalories = mutableListOf<String>()
        val orderfooding = mutableListOf<String>()
        val orderItemQuantity = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    //get all cart items
                    val orderItems = foodsnapshot.getValue(CartItem::class.java)
                    //add items details into list
                    orderItems?.foodItemName?.let { orderfoodnames.add(it) }
                    orderItems?.foodItemPrice?.let { orderfoodPrice.add(it) }
                    orderItems?.foodItemQuantity?.let { orderItemQuantity.add(it) }
                    orderItems?.foodItemCalories?.let { orderfoodCalories.add(it) }
                    orderItems?.foodItemCategory?.let { orderfoodCategory.add(it) }
                    orderItems?.foodImage?.let { orderfoodImg.add(it) }
                    orderItems?.foodItemIngredients?.let { orderfooding.add(it) }
                    Log.d("item", orderItems?.foodItemName.toString())
                }
                orderFoodNow(
                    orderfoodnames,
                    orderfoodPrice,
                    orderItemQuantity,
                    orderfoodImg,
                    orderfoodCalories,
                    orderfoodCategory,
                    orderfooding
                )
            }

            private fun orderFoodNow(
                orderfoodnames: MutableList<String>,
                orderfoodPrice: MutableList<String>,
                orderItemQuantity: MutableList<Int>,
                orderfoodImg: MutableList<String>,
                orderfoodCalories: MutableList<String>,
                orderfoodCategory: MutableList<String>,
                orderfooding: MutableList<String>
            ) {
                if(isAdded && context!=null){
                    val intent = Intent(requireContext(),CheckOutActivity::class.java)
                    intent.putExtra("ItemNames",orderfoodnames as ArrayList<String>)
                    intent.putExtra("ItemPrice",orderfoodPrice as ArrayList<String>)
                    intent.putExtra("ItemQuantity",orderItemQuantity as ArrayList<Int>)
                    intent.putExtra("ItemImg", orderfoodImg as ArrayList<String>)
                    intent.putExtra("ItemIngredient",orderfooding as ArrayList<String>)
                    intent.putExtra("ItemCalories",orderfoodCalories as ArrayList<String>)
                    intent.putExtra("ItemCategory",orderfoodCategory as ArrayList<String>)
                 //   Log.d("item",orderItemQuantity.size.toString())
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "order failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

   */


    private fun retriveCartItems() {
        //database reference to firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodItemRef: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        //list to store  Cart items
        cartItemName = mutableListOf()
        cartItemDesc = mutableListOf()
        cartItemPrice = mutableListOf()
        cartItemImageUri = mutableListOf()
        cartItemIngredients = mutableListOf()
        cartItemCategory = mutableListOf()
        cartItemCalories = mutableListOf()
        cartItemQuantity = mutableListOf()

        //fetch data from the database
        foodItemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (cartfoodsnapshot in snapshot.children) {

                    //get cart items object from child node
                    val cartItems = cartfoodsnapshot.getValue(CartItem::class.java)
                    cartItems?.foodItemName?.let { cartItemName.add(it) }
                    cartItems?.foodItemPrice?.let { cartItemPrice.add(it) }
                    cartItems?.foodItemPrice?.let { cartItemPrice.add(it) }
                    cartItems?.foodImage?.let { cartItemImageUri.add(it) }
                    cartItems?.foodItemQuantity?.let { cartItemQuantity.add(it) }
                    cartItems?.foodItemCalories?.let { cartItemCalories.add(it) }
                    cartItems?.foodItemIngredients?.let { cartItemIngredients.add(it) }
                    cartItems?.foodItemCategory?.let { cartItemCategory.add(it) }
                }
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "data not found", Toast.LENGTH_SHORT).show()

            }

        })

    }

    private fun setAdapter() {
        cartAdapter = CartAdapter(
            requireContext(),
            cartItemName,
            cartItemPrice,
            cartItemDesc,
            cartItemIngredients,
            cartItemCategory,
            cartItemCalories,
            cartItemImageUri,
            cartItemQuantity
        )
        binding.cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartRecyclerView.adapter = cartAdapter

    }


    companion object {

    }
}

