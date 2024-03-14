package com.example.cusineclick

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cusineclick.Fragment.HomeFragment
import com.example.cusineclick.databinding.ActivityCheckOutBinding
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat


class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userinfo: UserModel
    private lateinit var uid: String
    private lateinit var database:DatabaseReference
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

            // By invoking the notificationChannel() function for registering our channel to the System
            notificationChannel()

            // Building the notification
            val nBuilder = NotificationCompat.Builder(this, CHANNEL_ID)

                // adding notification Title
                .setContentTitle("CuisineClick")

                // adding notification Message
                .setContentText("Order Has been Placed Successfully")

                // adding notification SmallIcon
                .setSmallIcon(R.drawable.notification)

                // adding notification Priority
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .build()
            // finally notifying the notification
            val notificationManager = NotificationManagerCompat.from(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setOnClickListener
            }
            notificationManager.notify(1, nBuilder)
            val intent = Intent(this, HomeFragment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            finish()
        }
    }


    fun notificationChannel() {
        // check if the version is equal or greater than android oreo version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // creating notification channel and setting the description of the channel
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            // registering the channel to the System
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun transferdata() {
        val timestamp = System.currentTimeMillis()
        val restaurantName = intent.getStringExtra("RestaurantName")
        val destinationRef = database.child("Order").child(uid).child(timestamp.toString()).child(restaurantName.toString())
        val sourceRef= database.child("User").child("UserData").child(uid).child("Cart").child(
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
                        destinationRef.child(timestamp.toString()).child(key!!).setValue(data)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Data copied successfully for this key
                                    Log.d("copy","copy data to order node")
                                } else {
                                    // Handle the error
                                }
                            }
                    }
                    removeDataFromCart()
                    destinationRef.child("OrderAmount").setValue(total)
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
