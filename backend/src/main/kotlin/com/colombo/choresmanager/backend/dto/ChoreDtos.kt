package com.colombo.choresmanager.backend.dto

import com.colombo.choresmanager.backend.model.RemoteChore
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.ZonedDateTime

data class CreateChoreRequest(
    @field:NotBlank
    val name: String,

    @field:Min(1)
    val intervalDays: Int,

    @field:NotNull
    val lastDoneAt: ZonedDateTime,
)

data class CompleteChoreRequest(
    @field:NotNull
    val completedAt: ZonedDateTime,
)

data class ChoreResponse(
    val id: Long,
    val name: String,
    val intervalDays: Int,
    val lastDoneAt: ZonedDateTime,
    val createdAt: ZonedDateTime,
)

fun RemoteChore.toResponse(): ChoreResponse = ChoreResponse(
    id = id,
    name = name,
    intervalDays = intervalDays,
    lastDoneAt = lastDoneAt,
    createdAt = createdAt,
)
