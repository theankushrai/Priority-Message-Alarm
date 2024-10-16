package com.example.prioritymessagealarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.util.Log

class AlarmHelper{
    private val TAG = "MainActivity"

    fun triggerAlarm(context: Context) {
        Log.d(TAG, "Alarm triggered!")
        // Schedule the alarm here
        val alarmTime = System.currentTimeMillis() + 5000 // Trigger alarm in 5 seconds
        scheduleAlarmNotification(context, alarmTime)
    }

    fun scheduleAlarmNotification(context: Context, alarmTime: Long) {
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
            setSound(soundUri, AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(
                AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

}

