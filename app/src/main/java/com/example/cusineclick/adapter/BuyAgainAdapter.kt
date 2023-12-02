package com.example.cusineclick.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.databinding.BuyAgainItemBinding
import com.example.cusineclick.model.OrderItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DecimalFormat

class BuyAgainAdapter(private val context: Context) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>()  {

    private var orderItems: List<OrderItem> = mutableListOf()


    fun updateList(orderItem: MutableList<OrderItem>) {
        orderItems = orderItem
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)

    }
    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount():Int = orderItems.size

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val df = DecimalFormat("0.00")
            binding.tvTotal.text = df.format(orderItems[position].OrderAmount)
            binding.tvRestName.text = "Cuisine Click Order"
        }

    }

}
