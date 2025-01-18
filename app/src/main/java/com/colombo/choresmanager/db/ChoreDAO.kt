package com.colombo.choresmanager.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.colombo.choresmanager.model.Chore
import java.time.LocalDateTime

@Dao
interface ChoreDAO {

    @Query("SELECT * FROM chore")
    fun getAllChores(): LiveData<List<Chore>>

    @Query("SELECT * FROM chore WHERE id = :choreId")
    fun getChoreById(choreId: Int): LiveData<Chore>

    @Insert
    fun addChore(chore: Chore): Long

    @Query("DELETE FROM chore WHERE id = :choreId")
    fun deleteChore(choreId: Int)

    @Query("UPDATE chore SET lastDoneAt = :lastDoneAt WHERE id = :choreId")
    fun updateLastDoneAt(choreId: Int, lastDoneAt: LocalDateTime)
}