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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context,
    private val CartItems: MutableList<String>,
    private val CartItemPrice: MutableList<String>,
    private val CartDescription: MutableList<String>,
    private val CartItemIngredients: MutableList<String>,
    private val CartItemCategory: MutableList<String>,
    private val CartItemCalories: MutableList<String>,
    private val CartImage: MutableList<String>,
    private val CartQuantity: MutableList<Int>,

) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


//firebase instance
    private val auth= FirebaseAuth.getInstance()
    var itemQuantities : IntArray = intArrayOf()
    companion object{
        private lateinit var  cartItemRef : DatabaseReference
    }

    //get updated quantity
    fun getUpdatedItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(CartQuantity)
        return itemQuantity
    }
    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartitemNumber = CartItems.size

        itemQuantities = IntArray(cartitemNumber){1}
        cartItemRef= database.reference.child("user").child(userId).child("CartItems")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = CartItems.size



    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities?.get(position)
                cartFoodName.text = CartItems[position]
                cartItemPrice.text = CartItemPrice[position]
                cartItemQuantity.text = quantity.toString()
                //load image using glide
                val uriString = CartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)

                minusButton.setOnClickListener {
                    decreseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
            }
        }

        private fun decreseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            } else if (itemQuantities[position] == 1) {
                binding.minusButton.setImageResource(R.drawable.delete)
                deleteItem(position)
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
                binding.minusButton.setImageResource(R.drawable.minus)
            }
        }

        private fun deleteItem(position: Int) {
           val positionRetrive = position
            getUniqueKeyAtPosition(positionRetrive){uniqueKey ->
                if (uniqueKey != null){
                    removeItem(position,uniqueKey)
                }
            }

        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if(uniqueKey != null){
                cartItemRef.child(uniqueKey).removeValue().addOnSuccessListener {
                    CartItems.removeAt(position)
                    CartImage.removeAt(position)
                    CartDescription.removeAt(position)
                    CartQuantity.removeAt(position)
                    CartItemIngredients.removeAt(position)
                    CartItemCalories.removeAt(position)
                    CartItemCategory.removeAt(position)
                    CartItemPrice.removeAt(position)
                    //uptdate item quantity
                    itemQuantities = itemQuantities.filterIndexed { index, i -> index!= position  }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,CartItems.size)
                    Toast.makeText(context,"Item removed From Cart Successfully",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context,"Failed to remove",Toast.LENGTH_SHORT).show()
                }
            }


        }

        private fun getUniqueKeyAtPosition(positionRetrive: Int, OnComplete:(String?)-> Unit) {
            cartItemRef.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   var uniqueKey : String?=null
                    //loop to check if items in cart or not
                    snapshot.children.forEachIndexed{
                        index, dataSnapshot ->
                        if (index == positionRetrive){
                            uniqueKey= dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    OnComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }




    }

}