package com.colombo.choresmanager.data

import androidx.lifecycle.LiveData
import com.colombo.choresmanager.model.Chore

interface ChoreSyncStrategy {
    fun observeChores(): LiveData<List<Chore>>
    suspend fun addChore(chore: Chore): Int
    suspend fun deleteChore(choreId: Int)
    suspend fun completeChore(choreId: Int): Int
    suspend fun refresh()
    fun getChore(choreId: Int): LiveData<Chore?>
}
