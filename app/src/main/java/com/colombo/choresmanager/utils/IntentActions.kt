package com.colombo.choresmanager.utils

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelStoreOwner
import java.time.ZonedDateTime

enum class IntentActions(val handler: (context: Context, intent: Intent) -> Unit) {
    SNOOZE (handler = { context, oldIntent ->
        val choreId = oldIntent.getIntExtra("choreId", -1)
        val choreName = oldIntent.getStringExtra("choreName") ?: ""

        val intent = Intent(
            context,
            com.colombo.choresmanager.receivers.IntentReceiver::class.java
        )
        intent.putExtra("choreId", choreId)
        intent.putExtra("choreName", choreName)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            context,
            choreId,
            intent,
            android.app.PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(android.app.Activity.ALARM_SERVICE) as android.app.AlarmManager
        val selectedDate = java.util.Calendar.getInstance().apply {
            timeInMillis = ZonedDateTime.now().plusDays(1).toEpochSecond() * 1000
        }

        val year = selectedDate.get(java.util.Calendar.YEAR)
        val month = selectedDate.get(java.util.Calendar.MONTH)
        val day = selectedDate.get(java.util.Calendar.DAY_OF_MONTH)

        val calendar = java.util.Calendar.getInstance()
        calendar.set(year, month, day, 8, 0)

        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }),
    COMPLETE (handler = { context, oldIntent ->
        val vm = androidx.lifecycle.ViewModelProvider(
            context as ViewModelStoreOwner,
            com.colombo.choresmanager.viewmodels.ViewModelFactory()
        )[com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel::class.java]

        val choreId = oldIntent.getIntExtra("choreId", -1)
        val choreName = oldIntent.getStringExtra("choreName") ?: ""
        val chore = vm.getChore(choreId).value
        if (chore != null) {
            vm.completeChore(choreId)

            val intent = Intent(
                context,
                com.colombo.choresmanager.receivers.IntentReceiver::class.java
            )
            intent.putExtra("choreId", choreId)
            intent.putExtra("choreName", choreName)
            val pendingIntent = android.app.PendingIntent.getBroadcast(
                context,
                choreId,
                intent,
                android.app.PendingIntent.FLAG_MUTABLE
            )

            val alarmManager = context.getSystemService(android.app.Activity.ALARM_SERVICE) as android.app.AlarmManager
            val selectedDate = java.util.Calendar.getInstance().apply {
                timeInMillis =
                    ZonedDateTime.now().plusDays(chore.intervalDays.toLong()).toInstant().toEpochMilli()
            }

            val year = selectedDate.get(java.util.Calendar.YEAR)
            val month = selectedDate.get(java.util.Calendar.MONTH)
            val day = selectedDate.get(java.util.Calendar.DAY_OF_MONTH)

            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, day, 8, 0)

            alarmManager.setExactAndAllowWhileIdle(
                android.app.AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    });

    companion object {
        fun executeHandlerIfActionMatches(context: Context, intent: Intent): Boolean {
            val action = intent.action
            val o = if (action == null) null else valueOf(action)
            if (o != null) {
                o.handler(context, intent)
                return true
            } else return false
        }
    }
}