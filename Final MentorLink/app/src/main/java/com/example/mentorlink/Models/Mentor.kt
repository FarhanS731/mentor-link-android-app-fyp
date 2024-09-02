package com.example.yourapp.models

data class Mentor(
    val name: String,
    val email: String,
    val location: GeoPoint,
    val categories: Map<String, Category>,
    val profileImage: String,
    val description: String,
    val messages: Map<String, Message>?
)
