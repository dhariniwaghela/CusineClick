package com.example.cusineclick.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.cusineclick.CheckOutActivity
import com.example.cusineclick.MainActivity
import com.example.cusineclick.databinding.FragmentConfirmOrderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfirmOrderBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentConfirmOrderBottomSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmOrderBottomSheetBinding.inflate(inflater, container, false)
        binding.btnHome.setOnClickListener(View.OnClickListener {
          goToHome()

        })

        return binding.root
    }

    fun goToHome(){
        dismiss()
        var intent = Intent(requireActivity(),MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up onBackPressed callback
        dialog?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                goToHome()
                return@setOnKeyListener true
            }
            false
        }
    }


}

