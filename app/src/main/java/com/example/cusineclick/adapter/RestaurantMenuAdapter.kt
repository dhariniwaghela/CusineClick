package com.example.cusineclick.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cusineclick.FoodDetailsActivity
import com.example.cusineclick.databinding.RestaurantSingleMenuItemBinding
import com.example.cusineclick.model.MenuItem

class RestaurantMenuAdapter (private val requireContext: Context) :
    RecyclerView.Adapter<RestaurantMenuAdapter.RestaurantMenuViewHolder>() {

    private var menuitems: List<MenuItem> = mutableListOf()

    fun updateList(MenuItems: MutableList<MenuItem>) {
        menuitems = MenuItems
        notifyDataSetChanged()

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val binding = RestaurantSingleMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantMenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuitems.size

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RestaurantMenuViewHolder(private val binding: RestaurantSingleMenuItemBinding) :
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
            val menuItem = menuitems[position]
            //create intent to open menu item detail activity
            val intent = Intent(requireContext, FoodDetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.itemName)
                putExtra("MenuItemImg", menuItem.itemImage)
                putExtra("MenuItemDesc", menuItem.itemDescriptions)
                putExtra("MenuItemIngredient", menuItem.itemIngredients)
                putExtra("MenuItemPrice", menuItem.itemPrice)
                putExtra("MenuItemCategory", menuItem.itemCategory)
                putExtra("MenuItemCalories", menuItem.itemCalories)
                putExtra("RestaurantName",menuItem.restaurantName)
                putExtra("RestaurantId",menuItem.restaurantId)
            }
            //start activity
            requireContext.startActivity(intent)

        }


        //set data into recycler view of food name,image and price
        fun bind(position: Int) {
            val menuItem = menuitems[position]
            binding.apply {
                tvRestItemName.text = menuItem.itemName
                tvRestItemPrice.text = "CA $${menuItem.itemPrice}"
                tvRestItemCalories.text = "CAL 0- ${menuItem.itemCalories}"
                val uri = Uri.parse(menuItem.itemImage)
                Glide.with(requireContext).load(uri).into(restItemImage)

            }

        }

    }
}
