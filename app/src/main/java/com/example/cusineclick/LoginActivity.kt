package com.example.cusineclick

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cusineclick.databinding.ActivityLoginBinding
import com.example.cusineclick.databinding.ActivityStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSingInClient: GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //inialization of firebase auth
        auth = Firebase.auth

        //inialization of firebase database
        database = FirebaseDatabase.getInstance()

        //inialization of google sign in client
        googleSingInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        //login with email and password
        binding.btnLogin.setOnClickListener {
            //get data from fields
            email = binding.editTextEmailAddress.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createUser()
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
            val signInIntent = googleSingInClient.signInIntent
            launcher.launch(signInIntent)

        }

    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, ChooseLocationActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        }

                    }
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show()

            }
        }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                updateUi(user)
                Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun updateUi(user: FirebaseUser?) {
        val mainintent = Intent(this, ChooseLocationActivity::class.java)
        startActivity(mainintent)
        finish()

    }
}
