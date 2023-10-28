package com.example.cusineclick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.databinding.BuyAgainItemBinding

class BuyAgainAdapter(private val buyAgainFoodname:List<String>, private val buyAgainFoodprice: ArrayList<String>,
                      private val buyAgainFoodimage:ArrayList<Int>) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)

    }
    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(buyAgainFoodname[position],buyAgainFoodprice[position],buyAgainFoodimage[position])
    }

    override fun getItemCount():Int = buyAgainFoodimage.size

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(foodname: String, foodprice: String,foodimage: Int) {
            binding.buyagainfoodname.text = foodname
            binding.buyagainfoodprice.text = foodprice
            binding.buyagainfoodimage.setImageResource(foodimage)
        }

    }


}
