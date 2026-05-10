package com.colombo.choresmanager.data

import androidx.lifecycle.LiveData
import com.colombo.choresmanager.db.ChoreDAO
import com.colombo.choresmanager.model.Chore
import java.time.LocalDateTime

class LocalChoreSyncStrategy(
    private val choreDao: ChoreDAO,
) : ChoreSyncStrategy {
    override fun observeChores(): LiveData<List<Chore>> = choreDao.getAllChores()

    override suspend fun addChore(chore: Chore): Int = choreDao.addChore(chore).toInt()

    override suspend fun deleteChore(choreId: Int) {
        choreDao.deleteChore(choreId)
    }

    override suspend fun completeChore(choreId: Int): Int {
        choreDao.updateLastDoneAt(choreId, LocalDateTime.now())
        return choreId
    }

    override suspend fun refresh() = Unit

    override fun getChore(choreId: Int): LiveData<Chore?> = choreDao.getChoreById(choreId)
}
