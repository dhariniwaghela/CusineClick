
package com.example.cusineclick.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cusineclick.FoodDetailsActivity
import com.example.cusineclick.databinding.PopularItemBinding
class PopularAdapter (private val items : List<String> ,
                      private val price : List<String>, private  val image: List<Int>,private val requireContext: Context) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item, price, images)

        holder.itemView.setOnClickListener(View.OnClickListener {

            val intent = Intent(requireContext, FoodDetailsActivity::class.java)
            intent.putExtra("MenuItemName", item)
            intent.putExtra("MenuItemImage", images)
            requireContext.startActivity(intent)

        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imagesView = binding.foodImagePopular

        fun bind(item: String, price: String, images: Int) {
            binding.foodNamePopular.text = item
            binding.foodPricePopular.text = price
            imagesView.setImageResource(images)
        }

    }

    internal interface SendMessage {
        fun sendData(foodname: String, foodprice:Int, foodImage: Int)
    }

}
