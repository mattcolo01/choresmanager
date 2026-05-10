package com.colombo.choresmanager.network

import com.colombo.choresmanager.model.Chore
import java.time.ZonedDateTime

data class AuthRequest(
    val username: String,
    val password: String,
)

data class AuthResponse(
    val username: String,
    val token: String,
)

data class RemoteChore(
    val id: Int,
    val name: String,
    val intervalDays: Int,
    val lastDoneAt: String,
    val createdAt: String,
)

data class CreateChoreRequest(
    val name: String,
    val intervalDays: Int,
    val lastDoneAt: String,
)

data class CompleteChoreRequest(
    val completedAt: String,
)

fun RemoteChore.toDomain(): Chore = Chore(
    id = id,
    name = name,
    intervalDays = intervalDays,
    lastDoneAt = ZonedDateTime.parse(lastDoneAt),
    createdAt = ZonedDateTime.parse(createdAt),
)
