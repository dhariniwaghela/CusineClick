package com.example.cusineclick.model

data class CartItem(
    var foodItemName: String? = null,
    var foodItemPrice: String? = null,
    var foodItemDesc: String? = null,
    var foodItemIngredients: String? = null,
    var foodItemCategory: String? = null,
    var foodItemCalories: String? = null,
    var foodImage: String? = null,
    var foodItemQuantity: Int? = null,
    var cartItemId:String?= null
)
