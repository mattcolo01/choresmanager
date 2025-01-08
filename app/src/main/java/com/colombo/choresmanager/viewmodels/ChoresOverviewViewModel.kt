package com.colombo.choresmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colombo.choresmanager.MainApplication
import com.colombo.choresmanager.model.Chore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChoresOverviewViewModel : ViewModel() {

    private val choreDao = MainApplication.choreDatabase.getChoreDao()
    val choreList : LiveData<List<Chore>> = choreDao.getAllChores()

    fun addChore(title: String, interval: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.addChore(Chore(name = title, intervalDays = interval, lastDoneAt = LocalDateTime.now()))
        }
    }

    fun deleteChore(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.deleteChore(id)
        }
    }

    fun completeChore(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.updateLastDoneAt(id, LocalDateTime.now())
        }
    }
}