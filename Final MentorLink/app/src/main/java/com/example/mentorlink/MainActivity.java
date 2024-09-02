package com.example.mentorlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

        private TextView nameTextView;
        private TextView emailTextView;
        private TextView professionTextView;
        private TextView experienceTextView;
        private TextView hourlyRateTextView;
        private TextView classroomTextView;
        private Button signOutButton;
        private FirebaseAuth auth;
        private String currentUserType;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                // Initialize Firebase Auth
                auth = FirebaseAuth.getInstance();

                // Initialize views
                nameTextView = findViewById(R.id.nameTextView);
                emailTextView = findViewById(R.id.emailTextView);
                professionTextView = findViewById(R.id.professionTextView);
                experienceTextView = findViewById(R.id.experienceTextView);
                hourlyRateTextView = findViewById(R.id.hourlyRateTextView);
                classroomTextView = findViewById(R.id.classroomTextView);
                signOutButton = findViewById(R.id.signOutButton);

                // Get current user's details from Firebase and display them
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser != null) {
                        String userId = currentUser.getUid();
                        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String userName = dataSnapshot.child("name").getValue(String.class);
                                        String userEmail = dataSnapshot.child("email").getValue(String.class);
                                        currentUserType = dataSnapshot.child("userType").getValue(String.class);

                                        nameTextView.setText("Name: " + userName);
                                        emailTextView.setText("Email: " + userEmail);

                                        if ("Student".equals(currentUserType)) {
                                                // If the current user is a student, display mentor details
                                                String mentorName = dataSnapshot.child("mentorName").getValue(String.class);
                                                String mentorEmail = dataSnapshot.child("mentorEmail").getValue(String.class);
                                                String profession = dataSnapshot.child("profession").getValue(String.class);
                                                Long experience = dataSnapshot.child("experience").getValue(Long.class);
                                                Long hourlyRate = dataSnapshot.child("hourlyRate").getValue(Long.class);

                                                professionTextView.setText("Mentor Profession: " + profession);
                                                experienceTextView.setText("Mentor Experience: " + experience);
                                                hourlyRateTextView.setText("Mentor Hourly Rate: " + hourlyRate);
                                        } else if ("Mentor".equals(currentUserType)) {
                                                // If the current user is a mentor, display student details
                                                String studentName = dataSnapshot.child("studentName").getValue(String.class);
                                                String studentEmail = dataSnapshot.child("studentEmail").getValue(String.class);

                                                classroomTextView.setVisibility(View.VISIBLE);
                                                classroomTextView.setText("In the classroom with: " + studentName + " (" + studentEmail + ")");
                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle database error
                                }
                        });
                }

                // Set click listener for sign-out button
                signOutButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                // Sign out the user
                                auth.signOut();

                                // Redirect to the login activity
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                        }
                });
        }

        public void openProfileActivity(View view) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
        }
}
