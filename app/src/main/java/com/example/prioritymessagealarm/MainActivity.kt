package com.example.prioritymessagealarm

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.prioritymessagealarm.ui.theme.PriorityMessageAlarmTheme

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for notification permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_PERMISSION_CODE
            )
        }

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

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, trigger the alarm
                triggerAlarm()
                Log.d(TAG, "Permission granted to post notifications")
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Permission denied to post notifications", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Permission denied to post notifications")
            }
        }
    }

    private fun triggerAlarm() {
        Log.d(TAG, "Alarm triggered!")
        // Schedule the alarm here
        val context = this
        val alarmTime = System.currentTimeMillis() + 5000 // Trigger alarm in 5 seconds
        scheduleAlarmNotification(context, alarmTime)
    }

    @Composable
    fun TriggerAlarmNotification() {
        val context = LocalContext.current
        val alarmTime = System.currentTimeMillis() + 5000 // Trigger alarm in 5 seconds for testing

        // Automatically schedule the alarm when this composable is first launched
        LaunchedEffect(Unit) {
            scheduleAlarmNotification(context, alarmTime)
        }
    }

    private fun scheduleAlarmNotification(context: Context, alarmTime: Long) {
        createNotificationChannel(context) // Create the notification channel
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Set the alarm to trigger the receiver
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        Log.d(TAG, "Alarm set for $alarmTime")
    }

    private fun createNotificationChannel(context: Context) {
        val channelId = "full_screen_alarm"
        val channelName = "Full-Screen Alarm"
        val importance = NotificationManager.IMPORTANCE_HIGH

        // Define the custom sound URI
        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/raw/alarm_sound") // Use your sound file name here

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = "Channel for full-screen alarm notifications"
            setSound(soundUri, AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

