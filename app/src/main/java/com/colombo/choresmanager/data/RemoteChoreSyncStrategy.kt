package com.colombo.choresmanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.colombo.choresmanager.model.Chore
import com.colombo.choresmanager.network.ChoresApi
import com.colombo.choresmanager.network.CompleteChoreRequest
import com.colombo.choresmanager.network.CreateChoreRequest
import com.colombo.choresmanager.network.toDomain
import java.time.ZonedDateTime

class RemoteChoreSyncStrategy(
    private val choresApi: ChoresApi,
) : ChoreSyncStrategy {
    private val choreCache = MutableLiveData<List<Chore>>(emptyList())
    private var token: String? = null

    fun setToken(value: String?) {
        token = value
    }

    private fun authHeader(): String {
        val currentToken = token ?: throw IllegalStateException("Missing auth token")
        return "Bearer $currentToken"
    }

    override fun observeChores(): LiveData<List<Chore>> = choreCache

    override suspend fun addChore(chore: Chore): Int {
        val created = choresApi.createChore(
            bearerToken = authHeader(),
            request = CreateChoreRequest(
                name = chore.name,
                intervalDays = chore.intervalDays,
                lastDoneAt = chore.lastDoneAt.toString(),
            ),
        )
        refresh()
        return created.id
    }

    override suspend fun deleteChore(choreId: Int) {
        choresApi.deleteChore(
            bearerToken = authHeader(),
            choreId = choreId,
        )
        refresh()
    }

    override suspend fun completeChore(choreId: Int): Int {
        choresApi.completeChore(
            bearerToken = authHeader(),
            choreId = choreId,
            request = CompleteChoreRequest(ZonedDateTime.now().toString()),
        )
        refresh()
        return choreId
    }

    override suspend fun refresh() {
        val chores = choresApi.getChores(authHeader()).map { it.toDomain() }
        choreCache.postValue(chores)
    }

    override fun getChore(choreId: Int): LiveData<Chore?> {
        val chore = MediatorLiveData<Chore?>()
        chore.addSource(choreCache) { list ->
            chore.value = list.firstOrNull { it.id == choreId }
        }
        return chore
    }
}
