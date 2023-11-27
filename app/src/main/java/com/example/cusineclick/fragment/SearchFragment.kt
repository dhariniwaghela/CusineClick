package com.example.cusineclick.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.cusineclick.R
import com.example.cusineclick.adapter.MenuAdapter
import com.example.cusineclick.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter

    private val OriginalMenuFoodName =
        listOf("Burger", "Sandwhich", "Momo", "Item", "Manchurian", "Sushi")
    private val OriginalMenuItemPrice = listOf("$5", "$7", "$10", "$16", "$4", "$18")
    private val OriginalMenuItemImage = listOf<Int>(
        R.drawable.menu1,
        R.drawable.menu2,
        R.drawable.menu3,
        R.drawable.menu4,
        R.drawable.menu5,
        R.drawable.menu6
    )

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
     //   adapter = MenuAdapter(filterFoodName, filterFoodprice, filterFoodImage,requireContext())
       // binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
     //   binding.searchRecyclerView.adapter = adapter

        //setup for search view
        setupSearchView()

        //showall menu
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {

        filterFoodName.clear()
        filterFoodprice.clear()
        filterFoodImage.clear()
        filterFoodName.addAll(OriginalMenuFoodName)
        filterFoodprice.addAll(OriginalMenuItemPrice)
        filterFoodImage.addAll(OriginalMenuItemImage)
        adapter.notifyDataSetChanged()
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

        OriginalMenuFoodName.forEachIndexed { index, foodname ->
            if (foodname.contains(query.toString(), ignoreCase = true)) {
                filterFoodName.add(foodname)
                filterFoodprice.add(OriginalMenuItemPrice[index])
                filterFoodImage.add(OriginalMenuItemImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {
    }
}
