package com.colombo.choresmanager.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.colombo.choresmanager.model.Chore

@Dao
interface ChoreDAO {

    @Query("SELECT * FROM chore")
    fun getAllChores(): LiveData<List<Chore>>

    @Insert
    fun addChore(chore: Chore)

    @Query("DELETE FROM chore WHERE id = :choreId")
    fun deleteChore(choreId: Int)
}