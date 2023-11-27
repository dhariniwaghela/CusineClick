package com.example.cusineclick.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cusineclick.MainActivity
import com.example.cusineclick.databinding.FragmentConfirmOrderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmOrderBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentConfirmOrderBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentConfirmOrderBottomSheetBinding.inflate(inflater,container,false)
            binding.btnHome.setOnClickListener(View.OnClickListener {
                val intent = Intent (getActivity(), MainActivity::class.java)
                getActivity()?.startActivity(intent)
                dismiss()
            })
        return binding.root
    }

    companion object {

    }
}