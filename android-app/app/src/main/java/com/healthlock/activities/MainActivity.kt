package com.healthlock.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.healthlock.models.Record
import com.healthlock.network.ApiClient
import com.healthlock.utils.QRCodeGenerator
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var patientIdEdit: EditText
    private lateinit var uploadBtn: Button
    private lateinit var shareBtn: Button
    private lateinit var scanBtn: Button
    private lateinit var logsBtn: Button
    private lateinit var qrImage: ImageView
    private lateinit var filePath: String
    private var lastRecordId: String? = null
    private var selectedAccessLevel: String = "doctor"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        patientIdEdit = findViewById(R.id.patientIdEdit)
        uploadBtn = findViewById(R.id.uploadBtn)
        shareBtn = findViewById(R.id.shareBtn)
        scanBtn = findViewById(R.id.scanBtn)
        logsBtn = findViewById(R.id.logsBtn)
        qrImage = findViewById(R.id.qrImage)

        uploadBtn.setOnClickListener { openFilePicker() }
        shareBtn.setOnClickListener { shareRecord() }
        scanBtn.setOnClickListener { startQRScan() }
        logsBtn.setOnClickListener { viewAccessLogs() }

        // Spinner for access level
        val accessLevels = arrayOf("doctor", "pharmacist", "diagnostic")
        val spinner: Spinner = findViewById(R.id.accessLevelSpinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accessLevels)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                selectedAccessLevel = accessLevels[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data!!
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            val fileStr = Base64.encodeToString(bytes, Base64.DEFAULT)
            uploadRecord(fileStr)
        }
    }

    private fun uploadRecord(fileStr: String) {
        val patientId = patientIdEdit.text.toString()
        ApiClient.apiService.uploadRecord(patientId, fileStr, selectedAccessLevel).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                val body = response.body()
                if (body != null && body["success"] == true) {
                    lastRecordId = body["id"].toString()
                    Toast.makeText(this@MainActivity, "Upload successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Upload failed!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun shareRecord() {
        if (lastRecordId == null) {
            Toast.makeText(this, "Upload a record first!", Toast.LENGTH_SHORT).show()
            return
        }
        ApiClient.apiService.generateToken(lastRecordId!!, selectedAccessLevel).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                val token = response.body()?.get("token") ?: ""
                val qrBitmap: Bitmap = QRCodeGenerator.generate(token)
                qrImage.setImageBitmap(qrBitmap)
            }
            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Token error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // QR Scan using ZXing JourneyApps
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            accessWithToken(result.contents)
        }
    }

    private fun startQRScan() {
        val options = ScanOptions()
        options.setPrompt("Scan QR code")
        options.setBeepEnabled(true)
        barcodeLauncher.launch(options)
    }

    private fun accessWithToken(token: String) {
        val userId = "doctor123" // Replace with logged-in user's id
        ApiClient.apiService.accessWithToken(token, userId).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                val file = response.body?.get("file")
                Toast.makeText(this@MainActivity, "Accessed file: $file", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Access failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun viewAccessLogs() {
        if (lastRecordId == null) {
            Toast.makeText(this, "Upload a record first!", Toast.LENGTH_SHORT).show()
            return
        }
        ApiClient.apiService.getRecords(patientIdEdit.text.toString()).enqueue(object : Callback<List<Record>> {
            override fun onResponse(call: Call<List<Record>>, response: Response<List<Record>>) {
                val record = response.body()?.find { it._id == lastRecordId }
                if (record != null) {
                    val logs = record.accessLogs.joinToString("\n") { "${it.userId} - ${it.action} - ${it.timestamp}" }
                    Toast.makeText(this@MainActivity, logs, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "No logs found.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Record>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Log fetch failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
