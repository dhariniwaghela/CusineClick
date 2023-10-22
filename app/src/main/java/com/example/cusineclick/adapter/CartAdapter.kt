package com.example.cusineclick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.databinding.CartItemBinding

class CartAdapter (private val CartItems: MutableList<String> , private val CartItemPrice : MutableList<String> ,
                   private val CartImage:MutableList<Int>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


private  val itemQuantites =IntArray(CartItems.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val  binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
      holder.bind(position)
    }
    override fun getItemCount(): Int = CartItems.size
    inner class CartViewHolder (private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
           binding.apply {
               val quantity = itemQuantites[position]
               cartFoodName.text=CartItems[position]
               cartItemPrice.text= CartItemPrice[position]
               cartImage.setImageResource(CartImage[position])
               cartItemQuantity.text = quantity.toString()

               minusButton.setOnClickListener {
                    decreseQuantity(position)
               }
               plusButton.setOnClickListener {
                    increaseQuantity(position)
               }
               deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                   if (itemPosition != RecyclerView.NO_POSITION){
                       deleteItem(itemPosition)
                   }
               }

           }
        }
        private fun decreseQuantity(position: Int){
            if (itemQuantites[position] > 1){
                itemQuantites[position]--
                binding.cartItemQuantity.text = itemQuantites[position].toString()
            }
        }
        private fun increaseQuantity(position: Int){
            if (itemQuantites[position] < 10){
                itemQuantites[position]++
                binding.cartItemQuantity.text = itemQuantites[position].toString()
            }
        }
        private fun deleteItem(position: Int){
            CartItems.removeAt(position)
            CartItemPrice.removeAt(position)
            CartImage.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, CartItems.size)
        }

    }

}