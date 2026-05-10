package com.colombo.choresmanager.backend.security

import com.colombo.choresmanager.backend.model.UserSession
import com.colombo.choresmanager.backend.repository.UserSessionRepository
import org.springframework.stereotype.Service
import java.util.UUID
import java.time.ZonedDateTime

@Service
class TokenService(
    private val userSessionRepository: UserSessionRepository,
) {
    private val sessionDurationDays = 30L

    fun createTokenForUser(userId: Long): String {
        cleanupExpiredSessions()
        val token = UUID.randomUUID().toString()
        userSessionRepository.save(
            UserSession(
                token = token,
                userId = userId,
                expiresAt = ZonedDateTime.now().plusDays(sessionDurationDays),
            ),
        )
        return token
    }

    fun resolveUserId(token: String): Long? {
        cleanupExpiredSessions()
        val session = userSessionRepository.findById(token).orElse(null) ?: return null
        if (session.expiresAt.isBefore(ZonedDateTime.now())) {
            userSessionRepository.deleteById(token)
            return null
        }
        return session.userId
    }

    private fun cleanupExpiredSessions() {
        userSessionRepository.deleteAllByExpiresAtBefore(ZonedDateTime.now())
    }
}
