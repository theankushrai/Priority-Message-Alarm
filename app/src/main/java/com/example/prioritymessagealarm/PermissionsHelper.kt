package com.example.prioritymessagealarm

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsHelper {

    val REQUEST_PERMISSION_CODE = 1001
    val REQUEST_SMS_PERMISSION_CODE = 101 // Request code for SMS permission

    fun checkAndRequestNotificationPermission(context: Context, activity: Activity) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
               activity , arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_PERMISSION_CODE
            )
        }
    }
    fun checkAndRequestSmsPermission(context: Context,activity: Activity) {
        // Check if the SMS permission is already granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_SMS),
                REQUEST_SMS_PERMISSION_CODE
            )
        } else {
            // Proceed with SMS reading logic
        }
    }
}