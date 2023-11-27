package com.example.cusineclick.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.R
import com.example.cusineclick.adapter.MenuAdapter
import com.example.cusineclick.databinding.FragmentSearchBinding
import com.example.cusineclick.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val filterFoodName = mutableListOf<String>()
    private val filterFoodprice = mutableListOf<String>()
    private val filterFoodImage = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        retriveMenuItems()

        //setup for search view
     //   setupSearchView()


        return binding.root
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
                        menuItems.add(it)
                    }
                    //once data add set it to adapter
                    setAdapter()
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun setAdapter() {
        if(menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.searchRecyclerView.adapter = adapter
        }
        else
        {
            Log.d("Error","item is not set")
        }

    }


    private fun setupSearchView() {
        binding.seachView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }

        })

    }

    private fun filterMenuItems(query: String?) {
        filterFoodName.clear()
        filterFoodprice.clear()
        filterFoodImage.clear()

        menuItems.forEachIndexed { index, foodname ->
            if (filterFoodName.contains(query.toString())) {
                filterFoodName.add(foodname.toString())
                filterFoodprice.add(menuItems[index].toString())
               // filterFoodImage.add(OriginalMenuItemImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {
    }
}
