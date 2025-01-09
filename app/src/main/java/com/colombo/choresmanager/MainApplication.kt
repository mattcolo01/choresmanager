package com.colombo.choresmanager

import android.app.Application
import androidx.room.Room
import com.colombo.choresmanager.db.ChoreDatabase
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainApplication : Application() {

    companion object {
        lateinit var choreDatabase: ChoreDatabase
    }

    override fun onCreate() {
        super.onCreate()
        choreDatabase = Room.databaseBuilder(
            applicationContext, ChoreDatabase::class.java, "chores-database"
        ).fallbackToDestructiveMigration().build()

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainApplication) {}
        }
    }
}