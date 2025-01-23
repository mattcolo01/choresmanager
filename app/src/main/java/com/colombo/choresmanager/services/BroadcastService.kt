package com.colombo.choresmanager.services

import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import com.colombo.choresmanager.MainApplication
import com.colombo.choresmanager.utils.IntentActions
import com.colombo.choresmanager.utils.NotificationSender
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class BroadcastService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            val executed = executeHandlerIfActionMatches(intent)

            if (executed) {
            } else {
                val choreId = intent.getIntExtra("choreId", -1)
                val choreName = intent.getStringExtra("choreName") ?: ""
                if (choreId > -1) {
                    val scheduleNotificationSender = NotificationSender(this)
                    scheduleNotificationSender.sendReminderNotification(choreId, choreName)
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun executeHandlerIfActionMatches(intent: Intent): Boolean {
        if (intent.action == null) return false

        if (IntentActions.COMPLETE.name == intent.action) {
            completeHandler(intent)
            return true
        }

        if (IntentActions.SNOOZE.name == intent.action) {
            snoozeHandler(intent)
            return true
        }

        return true
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun completeHandler(oldIntent: Intent) {
        val dao = MainApplication.choreDatabase.getChoreDao()

        val choreId = oldIntent.getIntExtra("choreId", -1)
        val choreName = oldIntent.getStringExtra("choreName") ?: ""
        val observableDao = dao.getChoreById(choreId)

        observableDao.observe(this) { chore ->
            if (chore != null) {
                observableDao.removeObservers(this)

                GlobalScope.launch(Dispatchers.IO) {
                    dao.updateLastDoneAt(choreId, java.time.LocalDateTime.now())
                }

                val intent = Intent(
                    this,
                    com.colombo.choresmanager.receivers.IntentReceiver::class.java
                )
                intent.putExtra("choreId", choreId)
                intent.putExtra("choreName", choreName)
                val pendingIntent = android.app.PendingIntent.getBroadcast(
                    this,
                    choreId,
                    intent,
                    android.app.PendingIntent.FLAG_IMMUTABLE
                )

                val alarmManager = this.getSystemService(android.app.Activity.ALARM_SERVICE) as android.app.AlarmManager
                alarmManager.cancel(pendingIntent)
                val notificationManager = NotificationManagerCompat.from(this)
                notificationManager.cancel(choreId)

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
        }
    }

    private fun snoozeHandler(oldIntent: Intent) {
        val choreId = oldIntent.getIntExtra("choreId", -1)
        val choreName = oldIntent.getStringExtra("choreName") ?: ""

        val intent = Intent(
            this,
            com.colombo.choresmanager.receivers.IntentReceiver::class.java
        )
        intent.putExtra("choreId", choreId)
        intent.putExtra("choreName", choreName)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            this,
            choreId,
            intent,
            android.app.PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = this.getSystemService(android.app.Activity.ALARM_SERVICE) as android.app.AlarmManager
        val selectedDate = java.util.Calendar.getInstance().apply {
            timeInMillis = ZonedDateTime.now().plusDays(1).toEpochSecond() * 1000
        }

        val year = selectedDate.get(java.util.Calendar.YEAR)
        val month = selectedDate.get(java.util.Calendar.MONTH)
        val day = selectedDate.get(java.util.Calendar.DAY_OF_MONTH)

        val calendar = java.util.Calendar.getInstance()
        calendar.set(year, month, day, 8, 0)

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.cancel(intent.getIntExtra("choreId", -1))

        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}