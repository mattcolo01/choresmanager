package com.colombo.choresmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.colombo.choresmanager.model.Chore

@Database(entities = [Chore::class], version = 2)
@TypeConverters(Converter::class)
abstract class ChoreDatabase : RoomDatabase() {

    companion object {
        const val NAME = "chore_database"
    }

    abstract fun getChoreDao(): ChoreDAO
}