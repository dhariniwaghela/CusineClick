package com.example.cusineclick.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cusineclick.StartActivity
import com.example.cusineclick.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
           }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance();
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.btnLogout.setOnClickListener(View.OnClickListener {
                mAuth!!.signOut()
                val intent = Intent(activity, StartActivity::class.java)
                startActivity(intent)
            //logout but not kill fragment
            activity?.finish()

                Toast.makeText(activity, "Logout Successful !", Toast.LENGTH_SHORT).show()
            })

        return binding.root
    }

    companion object {
            }
}