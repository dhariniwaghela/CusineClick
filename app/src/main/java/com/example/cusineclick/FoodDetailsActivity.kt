package com.example.cusineclick

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cusineclick.databinding.ActivityFoodDetailsBinding
import com.example.cusineclick.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FoodDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailsBinding
    private var foodName: String? = null
    private var foodDesc: String? = null
    private var foodimg: String? = null
    private var foodIngredients: String? = null
    private var foodCategory: String? = null
    private var foodCalories: String? = null
    private var foodPrice: String? = null
    private var restaurantName : String? = null
    private var restaurantId:String? = null


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize firebase var
        auth = FirebaseAuth.getInstance()


        foodName = intent.getStringExtra("MenuItemName")
        foodDesc = intent.getStringExtra("MenuItemDesc")
        foodCalories = intent.getStringExtra("MenuItemCalories")
        foodIngredients = intent.getStringExtra("MenuItemIngredient")
        foodCategory = intent.getStringExtra("MenuItemCategory")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodimg = intent.getStringExtra("MenuItemImg")
        restaurantName = intent.getStringExtra("RestaurantName")
        restaurantId=intent.getStringExtra("RestaurantId")



        with(binding) {
            tvFoodname.text = foodName
            tvFoodCalories.text = "0 - $foodCalories Cal."
            tvFoodCategory.text = foodCategory
            tvFoodPrice.text = "CA $$foodPrice"
            tvFoodDescription.text = foodDesc
            tvFoodingredients.text = foodIngredients
            tvRestaurantname.text = "From: $restaurantName"
            Glide.with(this@FoodDetailsActivity).load(Uri.parse(foodimg)).into(foodImage)

        }


        //add to cart button
        binding.btnAddToCart.setOnClickListener {
            addItemtoCart()
        }


        binding.buttonBack.setOnClickListener {
            finish()
        }


    }

    private fun addItemtoCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""

        var cartItemRef = database.child("User").child("UserData").child(userId).child("Cart").child(restaurantName.toString()).push()
        val uniqueKey: String? = cartItemRef.key
        Log.d("key", uniqueKey.toString())
        //create cart item object
        val cartItem = CartItem(
            foodName.toString(),
            foodPrice.toString(),
            foodDesc.toString(),
            foodIngredients.toString(),
            foodCategory.toString(),
            foodCalories.toString(),
            foodimg.toString(),
            1,
            uniqueKey.toString(),
            restaurantName.toString(),
            restaurantId.toString()
        )


        //save data to cart item to firebase
        cartItemRef.setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item Added Successfully in Cart", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "Item Not Added in Cart", Toast.LENGTH_SHORT).show()
        }


    }

}