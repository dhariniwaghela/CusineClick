package com.example.cusineclick

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cusineclick.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    private lateinit var googleSingInClient: GoogleSignInClient

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

            if (email.isEmpty() || password.isBlank() ) {
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
            val signInIntent = googleSingInClient.signInIntent
            launcher.launch(signInIntent)

        }

    }

    //for google login
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intnet =Intent(this, MainActivity::class.java)
                            startActivity(intent)
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

    private fun LoginUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var isUserFound = false
                val userdatareference = database.reference.child("User").child("UserData")
                val valueEventListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (ds in dataSnapshot.children) {
                            val userEmail = ds.child("email").getValue(String::class.java)
                            val userPassword = ds.child("password").getValue(String::class.java)
                            isUserFound = userEmail == email && userPassword == password
                        }
                        if (isUserFound) {
                            val mainintent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(mainintent)
                            finish()
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successfull",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Account Does not exist ",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d(ContentValues.TAG, databaseError.getMessage()) //Don't ignore errors!
                        Toast.makeText(
                            this@LoginActivity,
                            "Account Does not exist ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                userdatareference.addListenerForSingleValueEvent(valueEventListener)

            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }


        }
    }

}
