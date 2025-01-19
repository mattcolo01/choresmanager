package com.colombo.choresmanager.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.colombo.choresmanager.MainActivity
import com.colombo.choresmanager.R

class NotificationSender(private val context: Context) {

    fun sendReminderNotification(choreId: Int, choreName: String) {
        val completeIntent = Intent(context, MainActivity::class.java).apply {
            action = IntentActions.COMPLETE.name
            putExtra("choreId", choreId)
            putExtra("choreName", choreName)
        }
        val snoozeIntent = Intent(context, MainActivity::class.java).apply {
            action = IntentActions.SNOOZE.name
            putExtra("choreId", choreId)
            putExtra("choreName", choreName)
        }
        val completePendingIntent = PendingIntent.getBroadcast(context, choreId, completeIntent, PendingIntent.FLAG_IMMUTABLE)
        val snoozePendingIntent = PendingIntent.getBroadcast(context, choreId, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val contentPendingIntent: PendingIntent = PendingIntent.getActivity(context, choreId, contentIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(ContextCompat.getString(context, R.string.almost_time_to) + choreName)
            .setContentText(ContextCompat.getString(context, R.string.dont_want_to))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
//            .addAction(R.mipmap.ic_launcher, ContextCompat.getString(context, R.string.complete), completePendingIntent)
//            .addAction(R.mipmap.ic_launcher, ContextCompat.getString(context, R.string.snooze_default), snoozePendingIntent)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(choreId, builder.build())
            }
        }
    }

}