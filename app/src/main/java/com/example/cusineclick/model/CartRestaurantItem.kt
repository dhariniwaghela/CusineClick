package com.example.cusineclick.model

data class CartRestaurantItem(
    val restaurantName:String?=null,
    val cartItem: ArrayList<CartItem>
)
