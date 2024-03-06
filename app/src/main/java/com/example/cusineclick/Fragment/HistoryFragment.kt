package com.example.cusineclick.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.adapter.BuyAgainAdapter
import com.example.cusineclick.databinding.FragmentHistoryBinding
import com.example.cusineclick.model.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var orderhistoryref: DatabaseReference
    private lateinit var userId: String
    private lateinit var orderItems: MutableList<OrderItem>
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
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        //initialize firebase
        auth = FirebaseAuth.getInstance()
        //database reference to firebase
        userId = auth.currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance()
        orderItems = mutableListOf()

        setAdapter()

        retriveOrderHistory()
        return binding.root
    }

    private fun retriveOrderHistory() {
        //database reference to firebase
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        orderhistoryref =
            database.reference.child("Order").child(userId)
        orderhistoryref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ordersnapshot in snapshot.children) {
                    val orderItem = ordersnapshot.getValue(OrderItem::class.java)
                    if (orderItem != null) {
                        orderItem.OrderRestaurantName= ordersnapshot.key
                    }
                    orderItem?.let {
                        orderItems.add(it)
                    }
                    buyAgainAdapter.updateList(orderItems)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter() {
        buyAgainAdapter = BuyAgainAdapter(requireContext())
        binding.buyagainrecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.buyagainrecyclerview.adapter = buyAgainAdapter

    }
}