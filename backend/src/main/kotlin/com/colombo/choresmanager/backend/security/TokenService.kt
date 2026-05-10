package com.colombo.choresmanager.backend.security

import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Service
class TokenService {
    private val tokenToUserId = ConcurrentHashMap<String, Long>()

    fun createTokenForUser(userId: Long): String {
        val token = UUID.randomUUID().toString()
        tokenToUserId[token] = userId
        return token
    }

    fun resolveUserId(token: String): Long? = tokenToUserId[token]
}
