package com.example.cusineclick

import android.R
import android.R.attr.name
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityLoginBinding
import com.example.cusineclick.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var progressAlertDialog: AlertDialog

    val CUSTOM_PREF_NAME = "User_data"
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidString(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressAlertDialog = createProgressDialog(this)

        //inialization of firebase auth
        auth = Firebase.auth

        //login with email and password
        binding.btnLogin.setOnClickListener {
            //get data from fields
            email = binding.editTextEmailAddress.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isBlank()) {
                binding.editTextEmailAddress.error = "email should not be empty"
                binding.editTextPassword.error = "Password should not be empty"
            } else if (!isValidString(email)) {
                binding.editTextEmailAddress.error = "Invalid email format"
            } else if (password.length < 6) {
                binding.editTextPassword.error = "Password must have atleast 6 caracters"
            } else {
                displayProgressDialog()
                LoginUser()
            }

        }
        binding.tvsignup.setOnClickListener {
            val signupintent = Intent(this, SignUpActivity::class.java)
            startActivity(signupintent)
        }
        binding.tvforgotpassword.setOnClickListener {
            val signupintent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(signupintent)
        }

        binding.btnGoogle.setOnClickListener {
        }

    }

    private fun createProgressDialog(currentActivity: AppCompatActivity): AlertDialog {
        val vLayout = LinearLayout(currentActivity)
        vLayout.orientation = LinearLayout.VERTICAL
        vLayout.setPadding(50, 50, 50, 50)
        vLayout.addView(ProgressBar(currentActivity, null, R.attr.progressBarStyleLarge))
        return AlertDialog.Builder(currentActivity)
            .setCancelable(false)
            .setView(vLayout)
            .create()
    }

    fun displayProgressDialog() {
        if (!progressAlertDialog.isShowing()) {
            progressAlertDialog.show()
        }
    }

    fun hideProgressDialog() {
        progressAlertDialog.dismiss()
    }


    private fun LoginUser() {
        var isUserExist = -1
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                database = FirebaseDatabase.getInstance()
                val userDataReference = database.reference.child("User").child("UserData");
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            var userModel = ds.getValue(UserModel::class.java)!!
                            isUserExist = if(userModel!!.userId != null && ds.key == userModel!!.userId) 1 else 0
                            break
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        isUserExist = 0
                    }
                }
                userDataReference.addListenerForSingleValueEvent(valueEventListener)
            } else {
                isUserExist = 2
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            hideProgressDialog()
            when(isUserExist){
                0 -> Toast.makeText(
                    this@LoginActivity,
                    "Account may be  used by another user",
                    Toast.LENGTH_SHORT
                ).show()
                1 -> {
                        val sharedPreferences = getSharedPreferences("userPref", MODE_PRIVATE)
                        val myEdit = sharedPreferences.edit()
                        myEdit.putString("userId", auth.currentUser?.uid)
                        myEdit.apply()
                        val loginintent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(loginintent)
                }
                2 -> Toast.makeText(
                    this@LoginActivity,
                    "Account doesn't exist or Invalid credentials",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }, 4500) //millis

    }
}


