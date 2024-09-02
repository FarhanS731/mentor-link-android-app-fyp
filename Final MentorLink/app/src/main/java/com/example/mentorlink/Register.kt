package com.example.mentorlink

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextProfessions: TextInputEditText
    private lateinit var editTextExperience: TextInputEditText
    private lateinit var editTextHourlyRate: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var locationSpinner: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonMentor: RadioButton
    private lateinit var radioButtonStudent: RadioButton
    private lateinit var btnRegister: Button
    private lateinit var profession: TextView
    private lateinit var experience: TextView
    private lateinit var hourlyRate: TextView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("UserID")

        radioGroup = findViewById(R.id.radioGroup)
        radioButtonMentor = findViewById(R.id.radioButtonMentor)
        radioButtonStudent = findViewById(R.id.radioButtonStudent)

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextName = findViewById(R.id.name)
        editTextProfessions = findViewById(R.id.professions)
        editTextExperience = findViewById(R.id.experience)
        editTextHourlyRate = findViewById(R.id.hourly_rate)
        locationSpinner = findViewById(R.id.locationSpinner)

        profession = findViewById(R.id.professions)
        experience = findViewById(R.id.experience)
        hourlyRate = findViewById(R.id.hourly_rate)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonMentor -> {
                    // If Mentor is selected, show profession, experience, and hourly rate fields
                    profession.visibility = TextView.VISIBLE
                    experience.visibility = TextView.VISIBLE
                    hourlyRate.visibility = TextView.VISIBLE
                }
                R.id.radioButtonStudent -> {
                    // If Student is selected, hide profession, experience, and hourly rate fields
                    profession.visibility = TextView.GONE
                    experience.visibility = TextView.GONE
                    hourlyRate.visibility = TextView.GONE
                }
            }
        }

        btnRegister = findViewById(R.id.btn_Register)
        btnRegister.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                // No option selected
                Toast.makeText(this, "Please select Mentor or Student", Toast.LENGTH_SHORT).show()
            } else {
                // One option is selected
                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                val userType = selectedRadioButton.text.toString()

                // Proceed with the registration process
                registerUser(userType)
            }
        }

        // Initializing Places SDK
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        val placesClient: PlacesClient = Places.createClient(this)
        val bounds = RectangularBounds.newInstance(
            LatLng(51.427, -10.506), // South-West corner of Ireland
            LatLng(55.387, -5.117)   // North-East corner of Ireland
        )

        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("IE")
            .setLocationBias(bounds)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val predictions = response.autocompletePredictions
                val locationList = predictions.map { it.getFullText(null).toString() }
                initializeLocationSpinner(locationList)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Autocomplete prediction fetch failed: $exception")
                Toast.makeText(this@Register, "Failed to fetch location predictions", Toast.LENGTH_SHORT).show()
            }

        progressBar = findViewById(R.id.progressbar)
        auth = FirebaseAuth.getInstance()

        val textView: TextView = findViewById(R.id.loginNow)
        textView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initializeLocationSpinner(locations: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSpinner.adapter = adapter
    }

    private fun registerUser(userType: String) {
        val password: String = editTextPassword.text.toString()
        val email: String = editTextEmail.text.toString()
        val name: String = editTextName.text.toString()
        val professions: String = editTextProfessions.text.toString()
        val experience: Int = editTextExperience.text.toString().toIntOrNull() ?: 0
        val hourlyRate: Int = editTextHourlyRate.text.toString().toIntOrNull() ?: 0

        progressBar.visibility = View.VISIBLE

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            Toast.makeText(this@Register, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val userId = currentUser?.uid

                    val userData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "userType" to userType,
                        "professions" to professions,
                        "experience" to experience,
                        "hourlyRate" to hourlyRate,
                        "password" to password // Add password to the user data
                    )

                    userId?.let {
                        databaseReference.child(it).setValue(userData)
                            .addOnSuccessListener {
                                Log.d(TAG, "User data added successfully")
                                val intent = Intent(this, Login::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding user data", e)
                                Toast.makeText(this@Register, "Error adding user data", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    progressBar.visibility = View.GONE
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}
