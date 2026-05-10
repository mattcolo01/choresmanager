package com.colombo.choresmanager.backend.repository

import com.colombo.choresmanager.backend.model.UserSession
import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime

interface UserSessionRepository : JpaRepository<UserSession, String> {
    fun deleteAllByExpiresAtBefore(timestamp: ZonedDateTime)
}
