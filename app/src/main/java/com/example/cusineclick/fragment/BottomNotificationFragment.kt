package com.example.cusineclick.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cusineclick.adapter.NotificationAdapter
import com.example.cusineclick.databinding.FragmentNotificationBottomBinding
import com.example.cusineclick.model.BannerInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BottomNotificationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var bannerInfo: MutableList<BannerInfo>
    private lateinit var adapter: NotificationAdapter

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

        setAdapter()
        retriveNotifications()
        return binding.root
    }

    private fun setAdapter() {
        adapter = NotificationAdapter(requireContext())
        binding.notificationReylcerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationReylcerView.adapter = adapter


    }

    private fun retriveNotifications() {
        database = FirebaseDatabase.getInstance()
        val offerRef: DatabaseReference = database.reference.child("Banner")
        bannerInfo = mutableListOf()
        offerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (offersnapshot in snapshot.children) {
                    val bannerInfoItem = offersnapshot.getValue(BannerInfo::class.java)
                    bannerInfoItem?.let {
                        bannerInfo.add(it)
                    }
                    //once data add set it to adapter
                    adapter.updateList(bannerInfo)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    companion object {

    }
}