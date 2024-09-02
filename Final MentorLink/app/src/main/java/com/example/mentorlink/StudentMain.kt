package com.example.yourapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mentorlink.R
import com.example.yourapp.models.Mentor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StudentMain : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var welcomeTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var mentorAdapter: MentorAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_main)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Reference to the TextView and RecyclerView
        welcomeTextView = findViewById(R.id.welcomeTextView)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        mentorAdapter = MentorAdapter(mutableListOf())
        recyclerView.adapter = mentorAdapter

        // Check if user is logged in
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Get current user's UID
            val userId = currentUser.uid

            // Reference to the Firestore document for the current user
            firestore.collection("students").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        // Set the name in the TextView
                        welcomeTextView.text = if (name != null) {
                            "Welcome, $name!"
                        } else {
                            "Welcome!"
                        }
                    } else {
                        welcomeTextView.text = "Welcome!"
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle error
                    Log.e("FirestoreError", "Error fetching user data: ", exception)
                    welcomeTextView.text = "Welcome!"
                }

            // Fetch mentor profiles
            fetchMentorProfiles()
        } else {
            // Handle case where no user is logged in
            welcomeTextView.text = "Welcome, Guest!"
        }
    }

    private fun fetchMentorProfiles() {
        firestore.collection("mentors")
            .get()
            .addOnSuccessListener { result ->
                val mentors = mutableListOf<Mentor>()
                for (document in result) {
                    val mentor = document.toObject(Mentor::class.java)
                    mentors.add(mentor)
                }
                // Update your UI with the mentors list
                displayMentors(mentors)
            }
            .addOnFailureListener { exception ->
                // Handle error
                Log.e("FirestoreError", "Error fetching mentors: ", exception)
            }
    }

    private fun displayMentors(mentors: List<Mentor>) {
        mentorAdapter.updateMentors(mentors)
    }
}
