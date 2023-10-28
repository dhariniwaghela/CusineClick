package com.example.cusineclick.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.R
import com.example.cusineclick.adapter.NotificationAdapter
import com.example.cusineclick.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NotificationBotomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): LinearLayout {
        // Inflate the layout for this fragment
        binding= FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        binding.buttonBack.setOnClickListener{
            dismiss()
        }

        val notification = listOf("Your order has been cancelled", "your order placed successfully","your order is on your way")
        val notificationimgs = listOf(R.drawable.sademoji, R.drawable.sademoji, R.drawable.sademoji)

        val adapter = NotificationAdapter(ArrayList(notification), ArrayList(notificationimgs))
        binding.notificationReylcerView.layoutManager= LinearLayoutManager(requireContext())
        binding.notificationReylcerView.adapter = adapter

        return binding.root
    }

    companion object {

    }
}