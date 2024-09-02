package com.example.mentorlink

data class User(
    val name: String? = null,
    val email: String? = null,
    val userType: String? = null,
    val profession: String? = null,
    val experience: Int? = null,
    val hourlyRate: Int? = null
) {
    val mentorEmail: String
        get() {
            TODO()
        }
    val mentorName: String
        get() {
            TODO()
        }
}

