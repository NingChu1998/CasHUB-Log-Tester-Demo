package com.example.cashub_logs_demo

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File

class LogWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val baseFolder = File(Environment.getExternalStorageDirectory(), "cashub_demo")
    private val logFileName = "demo_logs.csv"

    override fun doWork(): Result {
        return try {
            val logFile = saveLogcatToFile()
            if (logFile != null && logFile.exists()) {
                Log.d("LogWorker", "File saved at: ${logFile.absolutePath}")
                triggerAutoUpload(logFile.absolutePath)
                Result.success()
            } else {
                Log.e("LogWorker", "File capture failed")
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("LogWorker", "Error in background log task", e)
            Result.retry()
        }
    }

    private fun saveLogcatToFile(): File? {
        if (!baseFolder.exists()) {
            baseFolder.mkdirs()
        }

        val logFile = File(baseFolder, logFileName)
        
        try {
            // Match Scenario B/A behavior: Overwrite with fresh headers and full logcat
            logFile.writeText("Type,Tag,Message\n")
            logFile.appendText("INFO,CasHUB_Auto_Test,Background capture initialized successfully\n")

            // Execute logcat command (full capture, no filter, same as B)
            val command = "logcat -d -v csv"
            val process = Runtime.getRuntime().exec(command)

            process.inputStream.bufferedReader().use { reader ->
                reader.forEachLine { line ->
                    logFile.appendText(line + "\n")
                }
            }
            process.waitFor()
            
            return logFile
        } catch (e: Exception) {
            Log.e("LogWorker", "Save error", e)
            return null
        }
    }

    private fun triggerAutoUpload(fullPath: String) {
        val intent = Intent("cashub.active.upload")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("file_path", fullPath)
        intent.putExtra("package_name", applicationContext.packageName)
        applicationContext.sendBroadcast(intent)
        Log.d("LogWorker", "Sent active upload broadcast for $fullPath")
    }
}
