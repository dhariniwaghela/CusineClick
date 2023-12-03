package com.example.cusineclick

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private var isUserFound: Boolean = false
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

    private fun LoginUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val loginintent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(loginintent)
            } else {

                Toast.makeText(
                    this@LoginActivity,
                    "Account may be  used by another user",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }


}


