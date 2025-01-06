package com.colombo.choresmanager

import android.app.Application
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.colombo.choresmanager.db.ChoreDatabase

class MainApplication : Application() {

    companion object {
        lateinit var choreDatabase: ChoreDatabase
    }

    override fun onCreate() {
        super.onCreate()
        choreDatabase = Room.databaseBuilder(
            applicationContext, ChoreDatabase::class.java, "chores-database"
        ).fallbackToDestructiveMigration().build()
    }
}