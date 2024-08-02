package com.felixtien.fam.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.felixtien.fam.MainActivity
import com.felixtien.fam.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            // Pending intent for clicking the notification
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }
            // Build and display the notification
            val notification = NotificationCompat.Builder(this, "ID")
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setSmallIcon(R.drawable.notification) // Replace with your app icon
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            val manager = getSystemService(NotificationManager::class.java)
            manager.notify(0, notification)
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Additional code to handle the new token, e.g., sending it to a server or storing locally.
    }
}