package com.example.cashub_logs_demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cashub_demo.R
import java.io.File

class MainActivity : AppCompatActivity() {

    // Folder and file names matching your requirements
    private val logFolderPath = "/mnt/sdcard/cashub_demo"
    private val logFileName = "demo_logs.csv"

    private lateinit var tvStatus: TextView
    private lateinit var activeUploadReceiver: ActiveUploadReceiver

    // Must be placed inside the MainActivity class
    inner class ActiveUploadReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Retrieve the result code returned by CasHUB
            val resultCode = intent.getIntExtra("result_code", -1)
            if (resultCode == 0) {
                Toast.makeText(this@MainActivity, "Upload Success", Toast.LENGTH_SHORT).show()
                tvStatus.text = "CasHUB Status: SUCCESS"
            } else {
                Toast.makeText(this@MainActivity, "Upload Failed: $resultCode", Toast.LENGTH_SHORT).show()
                tvStatus.text = "CasHUB Status: FAILED ($resultCode)"
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        tvStatus = findViewById(R.id.tvStatus)
        val btnSaveLog = findViewById<Button>(R.id.btnSaveLog)
        val btnAutoUpload = findViewById<Button>(R.id.btnAutoUpload)

        // 1. Capture Logcat and save to SD card (for testing Manual Upload)
        btnSaveLog.setOnClickListener {
            val path = saveLogcatToFile()
            if (path != null) {
                tvStatus.text = "File Saved!\nPath: $path\n\nYou can now test Manual Upload on CasHUB."
            }
        }

        // 2. Trigger Active Upload broadcast (for testing Active Upload API)
        btnAutoUpload.setOnClickListener {
            triggerCasHubActiveUpload()
        }
    }

    private fun saveLogcatToFile(): String? {
        val folder = File(logFolderPath)
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val logFile = File(folder, logFileName)

        return try {
            // 1. Write a test log entry to the system cache first
            Log.d("TEST", "CasHUB_Demo: This is a manual test log message at ${System.currentTimeMillis()}")

            // 2. To ensure reliability, manually write a line of text to the file first
            // This ensures the file is not empty even if the logcat command fails
            logFile.writeText("Type,Tag,Message\n")
            logFile.appendText("DEBUG,CasHUB_Manual_Test,File initialized successfully\n")

            // 3. Execute logcat command and append the results to the file
            // Note: Command does not use -f; it uses the input stream for manual writing
            val command = "logcat -d -v csv"
            val process = Runtime.getRuntime().exec(command)

            // Read command output and write to file
            process.inputStream.bufferedReader().use { reader ->
                reader.forEachLine { line ->
                    logFile.appendText(line + "\n")
                }
            }

            process.waitFor()
            logFile.absolutePath
        } catch (e: Exception) {
            Log.e("CasHUB_Demo", "Save error", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun triggerCasHubActiveUpload() {
        val fullPath = "$logFolderPath/$logFileName"
        val file = File(fullPath)

        if (!file.exists()) {
            Toast.makeText(this, "Please save log first!", Toast.LENGTH_SHORT).show()
            return
        }

        // Implementation of Broadcast Intent based on Document Section 8.3
        val intent = Intent("cashub.active.upload")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("file_path", fullPath) //
        intent.putExtra("package_name", packageName) //
        sendBroadcast(intent) //

        tvStatus.text = "Broadcast Sent!\nWaiting for CasHUB response..."
    }

    // --- Inner Class: Receive CasHUB Upload Results ---

    override fun onResume() {
        super.onResume()
        // Register Broadcast Receiver to listen for upload results
        val filter = IntentFilter("com.castlestech.cashub.agent.action.UPLOAD")
        activeUploadReceiver = ActiveUploadReceiver()
        registerReceiver(activeUploadReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        // Unregister to save resources
        unregisterReceiver(activeUploadReceiver)
    }
}