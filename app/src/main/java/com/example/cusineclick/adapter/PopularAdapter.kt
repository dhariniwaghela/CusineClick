package com.example.cusineclick.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cusineclick.FoodDetailsActivity
import com.example.cusineclick.databinding.PopularItemBinding
import com.example.cusineclick.model.PopularMenuItem

class PopularAdapter(
    private val popularmenuitems: List<PopularMenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun getItemCount(): Int = popularmenuitems.size

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openIteminDetailsActivity(position)
                }

            })

        }

        private fun openIteminDetailsActivity(position: Int) {
            val popularMenuItem = popularmenuitems[position]
            //create intent to open menu item detail activity
            val intent = Intent(requireContext, FoodDetailsActivity::class.java).apply {
                putExtra("MenuItemName", popularMenuItem.itemName)
                putExtra("MenuItemImg", popularMenuItem.itemImage)
                putExtra("MenuItemDesc", popularMenuItem.itemDescriptions)
                putExtra("MenuItemIngredient", popularMenuItem.itemIngredients)
                putExtra("MenuItemPrice", popularMenuItem.itemPrice)
                putExtra("MenuItemCategory", popularMenuItem.itemCategory)
                putExtra("MenuItemCalories", popularMenuItem.itemCalories)
                putExtra("RestaurantName",popularMenuItem.restaurantName)
                putExtra("RestaurantId",popularMenuItem.restaurantId)
            }
            //start activity
            requireContext.startActivity(intent)

        }


        //set data into recycler view of food name,image and price
        fun bind(position: Int) {
            val popularmenuItem = popularmenuitems[position]
            binding.apply {
                foodNamePopular.text = popularmenuItem.itemName
                foodPricePopular.text = "$${popularmenuItem.itemPrice}"
                val uri = Uri.parse(popularmenuItem.itemImage)
                Glide.with(requireContext).load(uri).into(foodImagePopular)

            }

        }

    }
}
