package com.healthlock.network

object ApiClient {
    // Retrofit setup

    fun uploadRecord(filePath: String, accessLevel: String): String {
        // Call POST /api/records/upload
        // Return record ID
        return "recordId"
    }

    fun generateToken(recordId: String, accessLevel: String): String {
        // Call POST /api/access/generate-token
        // Return token
        return "JWT-Token"
    }

    fun accessWithToken(token: String): String {
        // Call POST /api/access/access-with-token
        // Return decrypted file
        return "decrypted file"
    }

    fun fetchLogs(recordId: String): List<String> {
        // Call GET /api/records/:id/logs
        return emptyList()
    }
}
