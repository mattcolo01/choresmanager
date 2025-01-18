package com.colombo.choresmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colombo.choresmanager.MainApplication
import com.colombo.choresmanager.model.Chore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class ChoresOverviewViewModel : ViewModel() {

    private val choreDao = MainApplication.choreDatabase.getChoreDao()
    val choreList : LiveData<List<Chore>> = choreDao.getAllChores()

    fun addChore(title: String, interval: Int, date: Long): MutableLiveData<Int> {
        val id = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            id.postValue(choreDao.addChore(Chore(
                name = title,
                intervalDays = interval,
                lastDoneAt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.systemDefault())
            )).toInt())
        }
        return id
    }

    fun deleteChore(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.deleteChore(id)
        }
    }

    fun completeChore(id: Int): MutableLiveData<Int> {
        val idWhenCompleted = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            choreDao.updateLastDoneAt(id, LocalDateTime.now())
            idWhenCompleted.postValue(id)
        }
        return idWhenCompleted
    }

    fun getChore(id: Int): LiveData<Chore> {
        return choreDao.getChoreById(id)
    }
}