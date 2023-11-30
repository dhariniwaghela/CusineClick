package com.example.cusineclick.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.R
import com.example.cusineclick.adapter.BuyAgainAdapter
import com.example.cusineclick.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
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
        binding =FragmentHistoryBinding.inflate(layoutInflater,container,false)

        setupRecyclerView()
        return binding.root
        }

    private fun setupRecyclerView(){
        val buyagainfoodPrice = arrayListOf("$5")

        buyAgainAdapter= BuyAgainAdapter(buyagainfoodPrice)
        binding.buyagainrecyclerview.adapter = buyAgainAdapter
        binding.buyagainrecyclerview.layoutManager = LinearLayoutManager(requireContext())

    }

    companion object {

    }
}