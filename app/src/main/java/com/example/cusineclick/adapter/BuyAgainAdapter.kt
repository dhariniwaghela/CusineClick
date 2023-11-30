package com.example.cusineclick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.databinding.BuyAgainItemBinding

class BuyAgainAdapter(private val buyAgainFoodprice: ArrayList<String>) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)

    }
    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodprice[position])
    }

    override fun getItemCount():Int = buyAgainFoodprice.size

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(foodprice: String) {
            binding.tvTotal.text = foodprice
        }

    }


}
