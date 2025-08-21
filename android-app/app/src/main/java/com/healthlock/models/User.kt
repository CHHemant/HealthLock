package com.healthlock.models

data class User(
    val userId: String,
    val name: String,
    val role: String // "doctor", "pharmacist", etc.
)
