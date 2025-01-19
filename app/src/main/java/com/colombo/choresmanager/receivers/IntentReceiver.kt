package com.colombo.choresmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.colombo.choresmanager.utils.IntentActions
import com.colombo.choresmanager.utils.NotificationSender

class IntentReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val executed = IntentActions.executeHandlerIfActionMatches(context, intent)

        if (!executed) {
            val choreId = intent.getIntExtra("choreId", -1)
            val choreName = intent.getStringExtra("choreName") ?: ""
            if (choreId > -1) {
                val scheduleNotificationSender = NotificationSender(context)
                scheduleNotificationSender.sendReminderNotification(choreId, choreName)
            }
        }
    }
}