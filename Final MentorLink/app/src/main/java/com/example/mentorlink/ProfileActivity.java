package com.example.mentorlink;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        TextView usernameTextView = findViewById(R.id.usernameTextView);

        if (usernameTextView != null) {
            // Set text for TextView
            usernameTextView.setText(R.string.username_description);
        } else {
            // Log a message or handle the case where TextView is not found
            Log.e("ProfileActivity", "TextView not found");
        }

        TextView emailTextView = findViewById(R.id.emailTextView);
        ImageView profilePictureImageView = findViewById(R.id.profilePictureImageView);
        Button backToMainButton = findViewById(R.id.backToMainButton);

        // Get the currently logged-in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Display user details
            String username = user.getDisplayName();
            String email = user.getEmail();

            // Check if the username is available
            if (username != null && !username.isEmpty()) {
                usernameTextView.setText(username);
            } else {
                // If username is not available, set a default value
                usernameTextView.setText(R.string.anonymous);
            }

            emailTextView.setText(email);
        } else {
            // If no user is logged in, display an error or redirect to login
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            // Example: Redirect to login activity
            // startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            // finish();
        }

        // Set click listener for back to main button
        backToMainButton.setOnClickListener(v -> {
            // Navigate back to MainActivity
            finish(); // Close the ProfileActivity
        });
    }
}
