package com.example.cusineclick.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.databinding.NotificationItemBinding
import com.example.cusineclick.model.BannerInfo

class NotificationAdapter(private val requireContext: Context) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    private var notification:List<BannerInfo> = mutableListOf()

    fun updateList(BannerItems: MutableList<BannerInfo>){
        notification = BannerItems
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int = notification.size


    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class NotificationViewHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val banneritem = notification[position]
            binding.apply {
                notificationtext.text = banneritem.bannerDescription
            }
        }

    }

}