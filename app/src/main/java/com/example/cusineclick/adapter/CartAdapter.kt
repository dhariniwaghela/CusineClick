package com.example.cusineclick.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cusineclick.R
import com.example.cusineclick.databinding.CartItemBinding
import com.example.cusineclick.model.CartItem
import com.example.cusineclick.model.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    private var cartitems: List<CartItem> = mutableListOf()
    //firebase instance
    private val auth = FirebaseAuth.getInstance()
    private var itemQuantities: IntArray = intArrayOf()
    private lateinit var cartItemRef: DatabaseReference

    fun updateList(cartItem: MutableList<CartItem>) {
        cartitems = cartItem
        notifyDataSetChanged()

    }


    //get updated quantity
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        cartItemRef = database.reference.child("User").child(userId).child("CartItems")
        itemQuantities = IntArray(cartitems.size) { 1 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartitems.size


    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                cartFoodName.text = cartitems[position].foodItemName.toString()
                cartItemPrice.text = "$${cartitems[position].foodItemPrice}"
                cartItemQuantity.text = cartitems[position].foodItemQuantity.toString()
                //load image using glide
                val uriString = cartitems[position].foodImage
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)

                minusButton.setOnClickListener {
                }
                plusButton.setOnClickListener {
                }

            }
        }

        private fun decreseQuantity(position: Int) {
            /*
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            } else if (itemQuantities[position] == 1) {
                binding.minusButton.setImageResource(R.drawable.delete)
                deleteItem(position)
            }*/
        }

        private fun increaseQuantity(position: Int) {
            /*
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
                binding.minusButton.setImageResource(R.drawable.minus)
            }

             */
        }

    }

}
