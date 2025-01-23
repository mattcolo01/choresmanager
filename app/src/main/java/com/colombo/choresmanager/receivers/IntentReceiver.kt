package com.colombo.choresmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.colombo.choresmanager.services.BroadcastService

class IntentReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val serviceIntent = Intent(context, BroadcastService::class.java)
        serviceIntent.action = intent.action
        serviceIntent.putExtras(intent.extras ?: return)
        context.startService(serviceIntent)
    }
}