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
import com.example.cusineclick.databinding.MenuItemBinding
import com.example.cusineclick.model.MenuItem

class MenuAdapter(private val menuitems:List<MenuItem>,
                  private val requireContext: Context)  : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuitems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
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
            }
            //start activity
            requireContext.startActivity(intent)

        }


        //set data into recycler view of food name,image and price
        fun bind(position: Int) {
            val menuItem = menuitems[position]
            binding.apply {
                menufoodName.text = menuItem.itemName
                menuprice.text = "$${menuItem.itemPrice}"
                val uri = Uri.parse(menuItem.itemImage)
                Glide.with(requireContext).load(uri).into(menuimage)


            }

        }

    }
}
