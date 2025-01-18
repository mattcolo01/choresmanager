package com.colombo.choresmanager.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.colombo.choresmanager.R
import com.colombo.choresmanager.utils.DEFAULT_CHANNEL_ID
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel
import com.colombo.choresmanager.viewmodels.ViewModelFactory

class NotificationService(private val context: Context) {

    fun sendReminderNotification(choreId: Int) {
        val choresOverviewViewModel = ViewModelProvider(context as ViewModelStoreOwner, ViewModelFactory())[ChoresOverviewViewModel::class.java]
        val chore = choresOverviewViewModel.getChore(choreId).value
        if (chore == null) {
            Log.w("NotificationService", "Chore not found, it may have been deleted")
            return
        }

        val builder = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(ContextCompat.getString(context, R.string.almost_time_to) + choreId)
            .setContentText(ContextCompat.getString(context, R.string.dont_want_to))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, ContextCompat.getString(context, R.string.complete), null)
            .addAction(R.mipmap.ic_launcher, ContextCompat.getString(context, R.string.snooze_default), null)

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