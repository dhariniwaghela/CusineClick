package com.example.cusineclick

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.Firebase.FcmApi
import com.example.cusineclick.Firebase.RequestNotification
import com.example.cusineclick.Firebase.SendNotificationModel
import com.example.cusineclick.databinding.ActivityCheckOutBinding
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo: UserModel
    private lateinit var uid: String
    private lateinit var database: DatabaseReference
    var total: Double = 0.0

    val CHANNEL_ID = "Notification"
    val CHANNEL_NAME = "NotificationChannelName"
    val CHANNEL_DESCRIPTION = "NotificationChanelDescription"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance();
        uid = auth.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("UserData")
        if (uid.isNotEmpty()) {
            //fetching user info
            getUserData()
        }

        val totalAmount = intent.getDoubleExtra("total", 0.0)
        binding.tvAmount.text = "CA$ $totalAmount"
        val df = DecimalFormat("##.##")

        val res: Double = totalAmount / 100.0 * 5.25

        binding.tvTax.text = "CA$ ${df.format(res)}"
        total = totalAmount + res
        binding.tvTotal.text = "CA$${df.format(total)}"
        binding.btncheckout.setOnClickListener {
            transferdata()
            /*
            val bottomSheetFragment = ConfirmOrderBottomSheetFragment()
            bottomSheetFragment.dialog?.setCancelable(false)
            bottomSheetFragment.dialog?.setCanceledOnTouchOutside(false)
            bottomSheetFragment.show(supportFragmentManager, "Test")

             */
        }

    }


    private fun transferdata() {
        val timestamp = System.currentTimeMillis()
        val restaurantName = intent.getStringExtra("RestaurantName")
        val destinationRef = database.child("Order").child(uid).child(timestamp.toString())
            .child(restaurantName.toString())
        val sourceRef = database.child("User").child("UserData").child(uid).child("Cart").child(
            restaurantName.toString()
        )
        sourceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        // Get the unique key
                        val key = childSnapshot.key

                        // Get the data from the source node
                        val data = childSnapshot.value

                        // Set the data in the destination node with the same key
                        destinationRef.child(key!!).setValue(data)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Data copied successfully for this key
                                    Log.d("copy", "copy data to order node")
                                } else {
                                    // Handle the error
                                }
                            }
                    }
                    sendAdminNotification(restaurantName)
                    destinationRef.child("OrderAmount").setValue(total)
                    removeDataFromCart()
                } else {
                    // Source node is empty or does not exist
                }
            }

            private fun removeDataFromCart() {
// Remove the cart item node and its data
                sourceRef.removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Node and data deleted successfully

                        } else {
                            // Handle the error
                        }
                    }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun sendAdminNotification(restaurantName: String?) {
        val sourceRef = database.child("User").child("UserData").child(uid).child("Cart").child(
            restaurantName.toString()
        )
        var restaurantId = ""
        sourceRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        val restaurnatData = childSnapshot.value as Map<String, Object>
                        restaurnatData.let {
                            restaurantId = restaurnatData["restaurantId"].toString()
                        }
                        if (restaurantId.isNotEmpty()) {
                            break
                        }
                    }
                    var token = ""
                    database.child("Admin").child("AdminData").child(restaurantId)
                            .addValueEventListener(object : ValueEventListener {
                                @SuppressLint("SuspiciousIndentation")
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val userObj = snapshot.value as Map<String, Object>
                                    userObj.let {
                                        token = userObj["firebasetoken"].toString()

                                        val sendNotificationModel =
                                            SendNotificationModel(
                                                "New Order Submitted",
                                                "New Order "
                                            )
                                        val requestNotificaton = RequestNotification()
                                        requestNotificaton.sendNotificationModel =
                                            sendNotificationModel
                                        //token is id , whom you want to send notification ,
                                        //token is id , whom you want to send notification ,
                                        requestNotificaton.token = token

                                        val apiService = getClient()?.create(FcmApi::class.java)
                                        val responseBodyCall: Call<ResponseBody> =
                                            apiService?.sendNotification(requestNotificaton)!!
                                        responseBodyCall.enqueue(object : Callback<ResponseBody> {
                                            override fun onResponse(
                                                call: Call<ResponseBody>,
                                                response: Response<ResponseBody>
                                            ) {
                                                if (response.isSuccessful) {
                                                    val fcmResponse = response.body()
                                                    // Handle successful response
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Task",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    // Handle error response
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<ResponseBody>,
                                                t: Throwable
                                            ) {
                                                // Handle failure
                                            }
                                        })
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userinfo = snapshot.getValue(UserModel::class.java)!!
                binding.tvName.text = userinfo.name
                binding.tvAddress.text = userinfo.location
                binding.tvcity.text = userinfo.city
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
