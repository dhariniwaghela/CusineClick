package com.example.cusineclick.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.CheckOutActivity
import com.example.cusineclick.Fragment.CartFragments
import com.example.cusineclick.databinding.RestaurantInCartBinding
import com.example.cusineclick.model.CartItem
import com.example.cusineclick.model.CartRestaurantItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CartRestaurantAdapter(private val context: Context,private val cartFragment: CartFragments) : RecyclerView.Adapter<CartRestaurantAdapter.CartViewHolder>() {


    private var cartRestaurantItem: ArrayList<CartRestaurantItem> = ArrayList()

    //firebase instance
    private val auth = FirebaseAuth.getInstance()
    private lateinit var cartItemRef: DatabaseReference


    fun updateCartRestaurantList(cartRestaurantitem: MutableList<CartRestaurantItem>) {
        cartRestaurantItem = cartRestaurantitem as ArrayList<CartRestaurantItem>
        notifyDataSetChanged()
    }

    //get updated quantity
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        cartItemRef = database.reference.child("User").child("UserData").child(userId).child("Cart")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = RestaurantInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartRestaurantItem.size


    inner class CartViewHolder(private val binding: RestaurantInCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                tvCartRestaurantName.text = cartRestaurantItem[position].restaurantName
                    var cartAdapter = CartAdapter(context,cartRestaurantItem[position].restaurantName,cartFragment)
                    cartItemRecyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    cartItemRecyclerView.adapter = cartAdapter

                cartAdapter.updateList(cartRestaurantItem[position].cartItem)

                tvCartRestaurantName.setOnClickListener{
                    var total = getOrderItemDetails(cartRestaurantItem[position].cartItem)
                    val intent = Intent(context, CheckOutActivity::class.java)
                    intent.putExtra("total", total)
                    intent.putExtra("RestaurantName",cartRestaurantItem[position].restaurantName)
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun getOrderItemDetails(cartItem: ArrayList<CartItem>): Double {
        var total = 0.0
                if (cartItem.size > 0) {
                    for (cartitem in cartItem) {
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

}



