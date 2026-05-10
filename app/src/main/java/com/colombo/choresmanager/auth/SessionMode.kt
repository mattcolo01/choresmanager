package com.colombo.choresmanager.auth

sealed class SessionMode {
    data object Guest : SessionMode()
    data class Authenticated(val username: String, val token: String) : SessionMode()
}
