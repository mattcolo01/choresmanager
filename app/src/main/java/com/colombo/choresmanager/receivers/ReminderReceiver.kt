package com.colombo.choresmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.colombo.choresmanager.services.NotificationService

class ReminderReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val scheduleNotificationService = context?.let { NotificationService(it) }
        val choreId = intent?.getIntExtra("choreId", -1) ?: -1
        if (choreId > -1) {
            scheduleNotificationService?.sendReminderNotification(choreId)
        }
    }
}