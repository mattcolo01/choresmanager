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

    val choreDao = MainApplication.choreDatabase.getChoreDao()
    val choreList : LiveData<List<Chore>> = choreDao.getAllChores()

    fun addChore(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.addChore(Chore(name = title, intervalDays = 7, lastDoneAt = LocalDateTime.now()))
        }
    }

    fun deleteChore(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.deleteChore(id)
        }
    }
}