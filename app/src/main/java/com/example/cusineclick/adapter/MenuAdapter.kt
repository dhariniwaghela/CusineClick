package com.example.cusineclick.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.databinding.MenuItemBinding

class MenuAdapter(private val menuitemsName: List<String>, private val menuitemprice: List<String>, private val menuImage: List<Int>)  : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
      val binding=MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int =menuitemsName.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
       holder.bind(position)
    }

    inner class MenuViewHolder(private val binding : MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                menufoodName.text = menuitemsName[position]
                menuprice.text = menuitemprice[position]
                menuimage.setImageResource(menuImage[position])
            }


    }

}
}