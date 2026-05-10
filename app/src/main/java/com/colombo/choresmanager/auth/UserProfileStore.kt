package com.colombo.choresmanager.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class UserProfileStore(context: Context) {
    private val prefs: SharedPreferences = try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "profiles",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    } catch (_: Exception) {
        context.getSharedPreferences("profiles", Context.MODE_PRIVATE)
    }

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
