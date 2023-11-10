package com.example.cusineclick.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.FoodDetailsActivity
import com.example.cusineclick.databinding.MenuItemBinding

class MenuAdapter(private val menuitemsName: List<String>, private val menuitemprice: List<String>, private val menuImage: List<Int>, private val requireContext: Context)  : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemclickListner: OnItemClickListener? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuitemsName.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemclickListner.onItemClick(position)
                }

            })
        }

        fun bind(position: Int) {
            binding.apply {
                menufoodName.text = menuitemsName[position]
                menuprice.text = menuitemprice[position]
                menuimage.setImageResource(menuImage[position])

            }


        }

    }

    private fun AdapterView.OnItemClickListener?.onItemClick(position: Int) {
        //set on menuitem click listener to open food details

        val intent = Intent(requireContext, FoodDetailsActivity::class.java)
        intent.putExtra("MenuItemName", menuitemsName.get(position))
        intent.putExtra("MenuItemImage", menuImage.get(position))
        requireContext.startActivity(intent)

    }
}