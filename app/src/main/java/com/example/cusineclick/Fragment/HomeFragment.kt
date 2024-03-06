package com.example.cusineclick.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.R
import com.example.cusineclick.adapter.RestaurantAdapter
import com.example.cusineclick.databinding.FragmentHomeBinding
import com.example.cusineclick.model.Restaurant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
  //  private lateinit var popularitems: MutableList<PopularMenuItem>

    private lateinit var restaurantlist: MutableList<Restaurant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance()


        //search view intent
        binding.tvseachView.setOnClickListener{
            
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, SearchFragment())
            transaction.addToBackStack("abc")
            transaction.commit()



        }


        binding.tvmenu.setOnClickListener {
            val bottomSheetdialog = MenuBotomSheetFragment()
            bottomSheetdialog.show(parentFragmentManager, tag)
        }

        //retrive all restaurants
        retriveRestaurantList()
        return binding.root

        //popular menu item retriving method
        // retrievePopularMenuItems()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        val imageList = ArrayList<SlideModel>()
        val imageSlider = binding.imageSlider
        database.reference.child("Banner")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (bannerSnapshot in snapshot.children) {
                       imageList.add(SlideModel(bannerSnapshot.child("bannerImage").value.toString(),ScaleTypes.FIT))
                        imageSlider.setImageList(imageList,ScaleTypes.FIT)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
                }

            })

         */
    }

    //showing all restaurant list
    private fun retriveRestaurantList() {

        val databaseReference: DatabaseReference = database.reference.child("Admin").child("AdminData")
        restaurantlist = mutableListOf()
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (restaurantsnapshot in snapshot.children) {
                    val restaurants = restaurantsnapshot.getValue(Restaurant::class.java)
                    restaurants?.let {
                        restaurantlist.add(it)
                    }
                    //display restaurants
                    setRestaurantAdapter(restaurantlist)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun setRestaurantAdapter(restaurants: List<Restaurant>) {
        val adapter = RestaurantAdapter(restaurants, requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }


    /*
    private fun retrievePopularMenuItems() {

        val foodRef: DatabaseReference = database.reference.child("menu")
        popularitems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    val popularMenuItem = foodsnapshot.getValue(PopularMenuItem::class.java)
                    popularMenuItem?.let {
                        popularitems.add(it)
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
        val randomInt = (5..10).random()
        val numofItem = randomInt
        val subsetmenuitem = index.take(numofItem).map { popularitems[it] }

        //once data add set it to adapter
        setPopularitemAdapter(subsetmenuitem)

    }

    private fun setPopularitemAdapter(subsetmenuitem: List<PopularMenuItem>) {
        val adapter = PopularAdapter(subsetmenuitem, requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }


     */


}