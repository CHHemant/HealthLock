package com.healthlock.models

data class AccessLog(
    val timestamp: String,
    val userId: String,
    val action: String
)

data class Record(
    val _id: String,
    val patientId: String,
    val file: String,
    val encrypted: Boolean,
    val accessLevel: String,
    val sharedTokens: List<SharedToken>,
    val accessLogs: List<AccessLog>
)

data class SharedToken(
    val token: String,
    val expires: String
)
