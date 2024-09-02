package com.example.mentorlink

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mentorlink.ui.adapter.StudentListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MentorMain : AppCompatActivity() {

    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var studentListAdapter: StudentListAdapter
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mentor_main)

        studentRecyclerView = findViewById<RecyclerView>(R.id.studentRecyclerView) ?: return

        val auth = FirebaseAuth.getInstance()

        // Initialize sign-out button
        val signOutButton: Button = findViewById<Button>(R.id.SignOutMentor)
        signOutButton.setOnClickListener {
            // Sign out the user
            auth.signOut()

            // Redirect to the login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        // Initialize the studentListAdapter
        studentListAdapter = StudentListAdapter(this, mutableListOf())
        studentRecyclerView.adapter = studentListAdapter
        studentRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("UserID")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val students: MutableList<Student> = mutableListOf()
                dataSnapshot.children.forEach { studentSnapshot ->
                    val student = studentSnapshot.getValue(Student::class.java)
                    if (student != null && student.userType == "Student") {
                        students.add(student)
                    }
                }
                studentListAdapter.updateData(students)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}
