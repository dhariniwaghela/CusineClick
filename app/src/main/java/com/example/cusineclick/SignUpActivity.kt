package com.example.cusineclick

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cusineclick.databinding.ActivitySignUpBinding
import com.example.cusineclick.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSingInClient: GoogleSignInClient


    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initialize firebase database
        auth = Firebase.auth
        //initialize database
        database = Firebase.database.reference

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.example.cusineclick.R.string.default_web_client_id))
            .requestEmail().build()

        //initialize google signin client
        googleSingInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        binding.btnSignup.setOnClickListener {
            username = binding.editTextName.text.toString()
            email = binding.editTextTextEmailAddress.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isBlank() || username.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }

        }

        binding.btnGoogle.setOnClickListener {
            val singInIntent = googleSingInClient.signInIntent
            launcher.launch(singInIntent)
        }


        binding.tvlogin.setOnClickListener {
            val loginintent = Intent(this, LoginActivity::class.java)
            startActivity(loginintent)
        }
    }

    //launcher for google sign in
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
                            Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show()
                        }

                    }
                } else {
                    Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show()

            }
        }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val mainintent = Intent(this, ChooseLocationActivity::class.java)
                startActivity(mainintent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "create Account:Failure", task.exception)

            }
        }
    }

    private fun saveUserData() {
        //retrive data
        username = binding.editTextName.text.toString()
        email = binding.editTextTextEmailAddress.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()

        val user = UserModel(username, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        //save user data
        database.child("user").child(userId).setValue(user)


    }

}