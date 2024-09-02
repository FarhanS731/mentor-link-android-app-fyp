package com.example.mentorlink

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yourapp.StudentMain
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already logged in, navigate to MainActivity
            navigateToMainActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressbar)
        auth = FirebaseAuth.getInstance()

        val textViewRegister: TextView = findViewById(R.id.RegisterNow)
        textViewRegister.setOnClickListener {
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener {
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString()
            progressBar.visibility = View.VISIBLE

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@Login, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@Login, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login successful, fetch user type from database
                        val currentUser = auth.currentUser
                        currentUser?.let { user ->
                            val userId = user.uid
                            val userRef = FirebaseDatabase.getInstance().getReference("UserID").child(userId)
                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val userType = snapshot.child("userType").getValue(String::class.java)
                                    if (!userType.isNullOrEmpty()) {
                                        progressBar.visibility = View.GONE
                                        if (userType == "Mentor") {
                                            // Navigate to MentorMain activity
                                            val intent = Intent(applicationContext, MentorMain::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else if (userType == "Student") {
                                            // Navigate to StudentMain activity
                                            val intent = Intent(applicationContext, StudentMain::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    } else {
                                        progressBar.visibility = View.GONE
                                        Toast.makeText(this@Login, "User type not found", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressBar.visibility = View.GONE
                                    Toast.makeText(this@Login, "Failed to fetch user type: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    } else {
                        // Authentication failed
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@Login, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun navigateToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
