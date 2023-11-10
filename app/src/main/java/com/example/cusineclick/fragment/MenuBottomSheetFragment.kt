package com.example.cusineclick.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.R
import com.example.cusineclick.adapter.MenuAdapter
import com.example.cusineclick.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LinearLayout {
        // Inflate the layout for this fragment
        binding= FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        binding.buttonBack.setOnClickListener{
            dismiss()
        }

        val MenuFoodName = listOf("Burger","Sandwhich","Momo","Item","Manchurian","Sushi")
        val MenuItemPrice = listOf("$5","$7","$10","$16","$4","$18")
        val MenuImage = listOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu5,
            R.drawable.menu6
        )
        val adapter = MenuAdapter(ArrayList(MenuFoodName) , ArrayList(MenuItemPrice) , ArrayList(MenuImage),requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root
   }

    companion object {
    }
}