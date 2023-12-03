package com.example.cusineclick.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cusineclick.EditProfileActivity
import com.example.cusineclick.R
import com.example.cusineclick.StartActivity
import com.example.cusineclick.databinding.FragmentProfileBinding
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo: UserModel
    private lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        auth = FirebaseAuth.getInstance();
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("UserData")
        if (uid.isNotEmpty()) {
            //fetching user info
            getUserData()
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener(View.OnClickListener {
            auth!!.signOut()
            val preferenceManager = activity?.getSharedPreferences("userPref", Context.MODE_PRIVATE)
            preferenceManager?.edit()?.clear()?.apply()
            val intent = Intent(activity, StartActivity::class.java)
            startActivity(intent)
            //logout but not kill fragment
            activity?.finish()
            Toast.makeText(activity, "Logout Successful !", Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue(UserModel::class.java) != null){
                    userinfo = snapshot.getValue(UserModel::class.java)!!
                    binding.textViewName.setText(userinfo.name)
                    binding.textViewEmail.setText(userinfo.email)

                    if(userinfo.location != null && userinfo.location!!.isNotEmpty() && userinfo.city != null && userinfo.city!!.isNotEmpty()){
                        binding.textViewAddress.text = "${userinfo.location},${userinfo.city}"
                    }
                    else {
                        binding.textViewAddress.text = ""
                    }
                    val imageUrl = userinfo.imgUri
                    activity?.let { Glide.with(it).load(imageUrl).placeholder(R.drawable.profile).error(R.drawable.profile).into(binding.profileImage) }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}