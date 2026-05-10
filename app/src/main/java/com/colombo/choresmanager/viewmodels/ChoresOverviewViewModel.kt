package com.colombo.choresmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colombo.choresmanager.MainApplication
import com.colombo.choresmanager.auth.SessionMode
import com.colombo.choresmanager.auth.UserProfileStore
import com.colombo.choresmanager.data.ChoreSyncStrategy
import com.colombo.choresmanager.data.LocalChoreSyncStrategy
import com.colombo.choresmanager.data.RemoteChoreSyncStrategy
import com.colombo.choresmanager.model.Chore
import com.colombo.choresmanager.network.ApiClient
import com.colombo.choresmanager.network.AuthRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

class ChoresOverviewViewModel : ViewModel() {
    private val localStrategy = LocalChoreSyncStrategy(MainApplication.choreDatabase.getChoreDao())
    private val remoteStrategy = RemoteChoreSyncStrategy(ApiClient.choresApi)
    private var currentStrategy: ChoreSyncStrategy = localStrategy
    private var strategySource: LiveData<List<Chore>>? = null
    private val profileStore = UserProfileStore(MainApplication.instance)

    private val _choreList = MediatorLiveData<List<Chore>>()
    val choreList: LiveData<List<Chore>> = _choreList

    private val _sessionMode = MutableLiveData<SessionMode>(SessionMode.Guest)
    val sessionMode: LiveData<SessionMode> = _sessionMode

    private val _showLoginScreen = MutableLiveData(true)
    val showLoginScreen: LiveData<Boolean> = _showLoginScreen

    private val _rememberedUsernames = MutableLiveData<List<String>>(emptyList())
    val rememberedUsernames: LiveData<List<String>> = _rememberedUsernames

    private val _authError = MutableLiveData<String?>(null)
    val authError: LiveData<String?> = _authError

    private val _isAuthLoading = MutableLiveData(false)
    val isAuthLoading: LiveData<Boolean> = _isAuthLoading

    init {
        switchStrategy(localStrategy)
        loadProfiles()
    }

    private fun switchStrategy(strategy: ChoreSyncStrategy) {
        strategySource?.let(_choreList::removeSource)
        currentStrategy = strategy
        val nextSource = strategy.observeChores()
        strategySource = nextSource
        _choreList.addSource(nextSource) { list ->
            _choreList.value = list
        }
    }

    private fun loadProfiles() {
        _rememberedUsernames.value = profileStore.getRememberedUsernames()
    }

    fun clearAuthError() {
        _authError.value = null
    }

    fun continueAsGuest() {
        _sessionMode.value = SessionMode.Guest
        remoteStrategy.setToken(null)
        switchStrategy(localStrategy)
        _showLoginScreen.value = false
    }

    fun login(username: String, password: String) {
        authenticate(username, password, register = false)
    }

    fun register(username: String, password: String) {
        authenticate(username, password, register = true)
    }

    private fun authenticate(username: String, password: String, register: Boolean) {
        if (username.isBlank() || password.isBlank()) {
            _authError.value = "Username and password are required"
            return
        }
        _isAuthLoading.value = true
        _authError.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = AuthRequest(username.trim(), password)
                val response = if (register) {
                    ApiClient.authApi.register(request)
                } else {
                    ApiClient.authApi.login(request)
                }
                profileStore.addRememberedUsername(response.username)
                _rememberedUsernames.postValue(profileStore.getRememberedUsernames())
                _sessionMode.postValue(SessionMode.Authenticated(response.username, response.token))
                remoteStrategy.setToken(response.token)
                withContext(Dispatchers.Main) {
                    switchStrategy(remoteStrategy)
                }
                remoteStrategy.refresh()
                _showLoginScreen.postValue(false)
            } catch (exception: HttpException) {
                val message = when (exception.code()) {
                    401 -> "Invalid username or password"
                    409 -> "Username already in use"
                    else -> "Authentication failed"
                }
                _authError.postValue(message)
            } catch (_: IOException) {
                _authError.postValue("Cannot reach backend server")
            } catch (_: Exception) {
                _authError.postValue("Authentication failed")
            } finally {
                _isAuthLoading.postValue(false)
            }
        }
    }

    fun logoutToLoginScreen() {
        _sessionMode.value = SessionMode.Guest
        remoteStrategy.setToken(null)
        _showLoginScreen.value = true
    }

    fun addChore(title: String, interval: Int, date: Long): MutableLiveData<Int> {
        val id = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val createdId = currentStrategy.addChore(
                Chore(
                    name = title,
                    intervalDays = interval,
                    lastDoneAt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.systemDefault()),
                ),
            )
            id.postValue(createdId)
        }
        return id
    }

    fun deleteChore(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            currentStrategy.deleteChore(id)
        }
    }

    fun completeChore(id: Int): MutableLiveData<Int> {
        val idWhenCompleted = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            idWhenCompleted.postValue(currentStrategy.completeChore(id))
        }
        return idWhenCompleted
    }

    fun getChore(id: Int): LiveData<Chore?> = currentStrategy.getChore(id)
}
