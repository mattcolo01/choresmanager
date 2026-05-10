package com.colombo.choresmanager.auth

import android.content.Context

class UserProfileStore(context: Context) {
    private val prefs = context.getSharedPreferences("profiles", Context.MODE_PRIVATE)

    fun getRememberedUsernames(): List<String> {
        return prefs.getStringSet(KEY_USERS, emptySet())?.sorted().orEmpty()
    }

    fun addRememberedUsername(username: String) {
        val current = prefs.getStringSet(KEY_USERS, emptySet()).orEmpty().toMutableSet()
        current.add(username)
        prefs.edit().putStringSet(KEY_USERS, current).apply()
    }

    companion object {
        private const val KEY_USERS = "remembered_users"
    }
}
