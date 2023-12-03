package com.example.cusineclick

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "User_data"
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            val sh = getSharedPreferences("userPref", MODE_PRIVATE)
            val userId = sh.getString("userId","")
            if (userId != null && userId.isNotEmpty()) {
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }else{
                startActivity(Intent(this@SplashScreen, StartActivity::class.java))
            }
            finish()
        }, 3000)
    }
}