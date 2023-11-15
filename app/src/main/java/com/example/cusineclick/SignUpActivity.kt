package com.example.cusineclick

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivitySignUpBinding
import com.example.cusineclick.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern


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
                binding.editTextName.error = "name should not be empty"
                binding.editTextTextEmailAddress.error = "email should not be empty"
                binding.editTextPassword.error = "Password should not be empty"
            } else if (!isValidString(email)) {
                binding.editTextTextEmailAddress.error = "Invalid email format"
            } else if (password.length < 6) {
                binding.editTextPassword.error = "Password must have atleast 6 caracters"
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

                sendVerificationEmail()
                // val mainintent = Intent(this, ChooseLocationActivity::class.java)
                // startActivity(mainintent)
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

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // email sent

                    // after email is sent just logout the user and finish this activity
                    Toast.makeText(this, "Verification link has been send", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, VerifyEmailActivity::class.java))
                    finish()
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do

                    //restart this activity
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                }
            }
    }

}