package com.example.cusineclick.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cusineclick.R
import com.example.cusineclick.databinding.CartItemBinding
import com.example.cusineclick.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CartAdapter(
    private val context: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    private var cartitems: ArrayList<CartItem> = ArrayList()

    //firebase instance
    private val auth = FirebaseAuth.getInstance()
    private lateinit var cartItemRef: DatabaseReference


    fun updateList(cartItem: MutableList<CartItem>) {
        cartitems = cartItem as ArrayList<CartItem>
        notifyDataSetChanged()
    }

    //get updated quantity
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        cartItemRef = database.reference.child("User").child("UserData").child(userId).child("CartItems")
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
                binding.minusButton.setImageResource(if (cartitems[position].foodItemQuantity == 1) R.drawable.delete else R.drawable.minus)

                minusButton.setOnClickListener {
                    if (cartitems[position].foodItemQuantity!! > 1) {
                        cartitems[position].foodItemQuantity =
                            cartitems[position].foodItemQuantity?.minus(
                                1
                            )
                        notifyItemChanged(position)
                        cartItemRef.child(cartitems[position].cartItemId!!)
                            .setValue(cartitems[position])

                    } else if (cartitems[position].foodItemQuantity == 1) {
                        cartItemRef.child(cartitems[position].cartItemId!!).removeValue()
                            .addOnSuccessListener {
                                notifyItemRemoved(position)
                                cartitems.removeAt(position)
                            }.addOnFailureListener {
                                Log.d("error", "item not deleted")
                            }

                    }
                }


                plusButton.setOnClickListener {

                    if (cartitems[position].foodItemQuantity!! < 10) {
                        cartitems[position].foodItemQuantity =
                            cartitems[position].foodItemQuantity?.plus(1)
                        notifyItemChanged(position)
                        cartItemRef.child(cartitems[position].cartItemId!!)
                            .setValue(cartitems[position])

                    } else {
                        // do not add more than 10 items
                    }

                }

            }

        }
    }
}



