package com.example.mentorlink
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mentorlink.ProfileActivity
import com.example.mentorlink.Login

class Header : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.header)
    }

    // Method to handle profile button click
    fun onProfileClicked(view: View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    // Method to handle sign out button click
    fun onSignOutClicked(view: View) {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}
