package com.healthlock.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.healthlock.network.ApiClient
import com.healthlock.utils.QRCodeGenerator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // UI: Upload file button, Share button, Scan QR button, Log button
    }

    // Upload record (PDF/Image)
    fun uploadRecord(filePath: String, accessLevel: String) {
        val response = ApiClient.uploadRecord(filePath, accessLevel)
        // Show record ID and option to share
    }

    // Generate QR code/token for sharing
    fun shareRecord(recordId: String, accessLevel: String) {
        val token = ApiClient.generateToken(recordId, accessLevel)
        val qrBitmap = QRCodeGenerator.generate(token)
        // Display QR code for doctor to scan
    }

    // Scan QR code from doctor side
    fun scanQRCodeActivity() {
        // Use camera to scan QR, send token to backend, fetch record
        val token = "scanned-token"
        val record = ApiClient.accessWithToken(token)
        // Show file/record
    }

    // Access logs
    fun viewAccessLogs(recordId: String) {
        val logs = ApiClient.fetchLogs(recordId)
        // Display logs (who/when accessed)
    }
}
