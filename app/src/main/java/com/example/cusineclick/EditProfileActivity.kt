package com.example.cusineclick

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cusineclick.databinding.ActivityEditProfileBinding
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private var userId: String? = null
    private lateinit var userinfo: UserModel
    var databaseReference: DatabaseReference? = null
    private var profileImageUri: Uri? = null

    var img_flag: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("UserData")
            .child(userId.toString())
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userinfo = snapshot.getValue(UserModel::class.java)!!
                    // Populate UI elements with retrieved data
                    binding.editTextName.setText(userinfo.name)
                    binding.editTextAddress.setText(userinfo.location)
                    binding.editTextCity.setText(userinfo.city)
                    val imageUrl = userinfo.imgUri.toString()
                    Glide.with(this@EditProfileActivity).load(imageUrl).placeholder(R.drawable.profile).error(R.drawable.profile)
                        .into(binding.addprofileImage)
                }

            }


            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })

        binding.addprofileImage.setOnClickListener {
            pickImage.launch("image/*")

        }

        binding.btnUpdate.setOnClickListener {
            if (isNameChanged() || isAddressChanged() || isCityChanged() || img_flag) {
                uploadImage()
                Toast.makeText(this, "Details Updated Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadImage() {
        if (profileImageUri != null) {
            val StorageRef: StorageReference = FirebaseStorage.getInstance().reference
            val imageRef: StorageReference = StorageRef.child("Profile_image/${userId}.jpg")
            val uploadTask: UploadTask = imageRef.putFile(profileImageUri!!)
            uploadTask.addOnCompleteListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    databaseReference?.child("imgUri")
                        ?.setValue(downloadUrl.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(this, " image not uploaded", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show()
        }

    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.addprofileImage.setImageURI(uri)
            profileImageUri = uri
            img_flag = true
        }
    }


    private fun isAddressChanged(): Boolean {
        return if (!userinfo.location.equals(binding.editTextAddress.text.toString())) {
            databaseReference?.child("location")?.setValue(binding.editTextAddress.text.toString())
            userinfo.location = binding.editTextAddress.text.toString()
            true
        } else {
            false
        }
    }

    private fun isCityChanged(): Boolean {
        return if (!userinfo.city.equals(binding.editTextCity.text.toString())) {
            databaseReference?.child("city")?.setValue(binding.editTextCity.text.toString())
            userinfo.city = binding.editTextCity.text.toString()
            true
        } else {
            false
        }
    }

    private fun isNameChanged(): Boolean {
        return if (!userinfo.name.equals(binding.editTextName.text.toString())) {
            databaseReference?.child("name")?.setValue(binding.editTextName.text.toString())
            userinfo.name = binding.editTextName.text.toString()
            true
        } else {
            false
        }
    }
}
