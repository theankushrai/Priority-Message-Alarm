package com.example.prioritymessagealarm

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.prioritymessagealarm.ui.theme.PriorityMessageAlarmTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for permissions
        PermissionsHelper().checkAndRequestNotificationPermission(this,this)
        PermissionsHelper().checkAndRequestSmsPermission(this,this)
        setContent {
            PriorityMessageAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        // Trigger the alarm as soon as the app is launched
//                        TriggerAlarmNotification()
                    }
                }
            }
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //notification permission
        if (requestCode == PermissionsHelper().REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AlarmHelper().triggerAlarm(this)
            } else {
                Toast.makeText(this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == PermissionsHelper().REQUEST_SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "READ_SMS permission granted", Toast.LENGTH_SHORT).show()
                // Proceed with SMS reading logic
            } else {
                Toast.makeText(this, "READ_SMS permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun TriggerAlarmNotification() {
        val context = LocalContext.current
        val alarmTime = System.currentTimeMillis() + 5000 // Trigger alarm in 5 seconds for testing

        // Automatically schedule the alarm when this composable is first launched
        LaunchedEffect(Unit) {
            AlarmHelper().scheduleAlarmNotification(context,alarmTime)
        }
    }
}

