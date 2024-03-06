package com.example.cusineclick.adapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cusineclick.RestaurantDetailActivity
import com.example.cusineclick.databinding.SingleRestaurantLayoutBinding
import com.example.cusineclick.model.Restaurant

class RestaurantAdapter(private val restaurants: List<Restaurant>, private val requireContext: Context)
    : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantViewHolder {
        val binding = SingleRestaurantLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
       holder.bind(position)
    }

    override fun getItemCount(): Int = restaurants.size


    inner class RestaurantViewHolder(private val binding: SingleRestaurantLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)  {
        init {
            binding.root.setOnClickListener(View.OnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openRestaurantPageActivity(position)
                }

            })

        }

        private fun openRestaurantPageActivity(position: Int) {
            val eachRestaurant = restaurants[position]
            //create intent to open menu item detail activity
            val intent = Intent(requireContext, RestaurantDetailActivity::class.java).apply {
                putExtra("RestaurantName", eachRestaurant.restaurantName)
                putExtra("RestaurantImg", eachRestaurant.imgUri)
                putExtra("RestaurantId",eachRestaurant.restaurantId)
            }
            //start activity
            requireContext.startActivity(intent)

        }


        //set data into recycler view of food name,image and price
        fun bind(position: Int) {
            val restaurant = restaurants[position]
            binding.apply {
                tvRestaurantName.text = restaurant.restaurantName
                val uriString = restaurant.imgUri
                if (!uriString.isNullOrBlank()) {
                    val uri = Uri.parse(uriString)
                    Glide.with(requireContext).load(uri).into(restaurantImage)
                } else {
                    // Handle case when URI is empty or null
                    // For example, you can set a placeholder image
                    // restaurantImage.setImageResource(R.drawable.placeholder_image)
                }
            }

        }

    }

}



