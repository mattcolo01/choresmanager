package com.colombo.choresmanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Chore(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String,
    var intervalDays: Int,
    var lastDoneAt: LocalDateTime,
    var createdAt: LocalDateTime = LocalDateTime.now(),
)