package com.example.cusineclick

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cusineclick.adapter.PopularAdapter
import com.example.cusineclick.adapter.RestaurantMenuAdapter
import com.example.cusineclick.databinding.ActivityRestaurantDetailBinding
import com.example.cusineclick.model.MenuItem
import com.example.cusineclick.model.PopularMenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    private lateinit var adapter: RestaurantMenuAdapter
    private var restaurantname:String? = null
    private var restaurantimguri:String?= null
    private var restaurantid:String?=null

    private lateinit var popularitems: MutableList<PopularMenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

         restaurantname = intent.getStringExtra("RestaurantName")
        binding.tvrestaurantname.text = restaurantname

        restaurantid = intent.getStringExtra("RestaurantId")
        restaurantimguri = intent.getStringExtra("RestaurantImg")
        val uri = Uri.parse(restaurantimguri)
        Glide.with(this).load(uri).into(binding.backgroundImage)


        binding.buttonBack.setOnClickListener {
            finish()
        }
        setAdapter()
        retriveMenuItems()
        retrievePopularMenuItems()

    }
    private fun retriveMenuItems() {

        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    val menuItem = foodsnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        if(menuItem.restaurantId.equals(restaurantid)) {
                            menuItems.add(it)
                        }
                    }
                    //once data add set it to adapter
                    adapter.updateList(menuItems)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun setAdapter() {
        adapter = RestaurantMenuAdapter(this)
        binding.restaurantmenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.restaurantmenuRecyclerView.adapter = adapter

    }


    private fun retrievePopularMenuItems() {

        val foodRef: DatabaseReference = database.reference.child("menu")
        popularitems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    val popularMenuItem = foodsnapshot.getValue(PopularMenuItem::class.java)
                    popularMenuItem?.let {
                        if (popularMenuItem.restaurantId.equals(restaurantid)) {
                            popularitems.add(it)
                        }
                    }
                    //display random popular items
                    randomPopularItems()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    //random popular item function
    private fun randomPopularItems() {
        //create shuffle list for popular item

        val index = popularitems.indices.toList().shuffled()
        val randomInt = (1..5).random()
        val numofItem = randomInt
        val subsetmenuitem = index.take(numofItem).map { popularitems[it] }

        //once data add set it to adapter
        setPopularitemAdapter(subsetmenuitem)

    }

    private fun setPopularitemAdapter(subsetmenuitem: List<PopularMenuItem>) {
        val adapter = PopularAdapter(subsetmenuitem, this)
        binding.popularItemRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.popularItemRecyclerView.adapter = adapter
    }



}