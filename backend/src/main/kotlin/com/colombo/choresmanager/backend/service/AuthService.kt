package com.colombo.choresmanager.backend.service

import com.colombo.choresmanager.backend.dto.AuthRequest
import com.colombo.choresmanager.backend.dto.AuthResponse
import com.colombo.choresmanager.backend.model.AppUser
import com.colombo.choresmanager.backend.repository.AppUserRepository
import com.colombo.choresmanager.backend.security.TokenService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val userRepository: AppUserRepository,
    private val tokenService: TokenService,
    private val passwordEncoder: PasswordEncoder,
) {
    fun register(request: AuthRequest): AuthResponse {
        if (userRepository.findByUsername(request.username).isPresent) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Username already in use")
        }

        val user = userRepository.save(
            AppUser(
                username = request.username,
                passwordHash = passwordEncoder.encode(request.password),
            ),
        )
        return AuthResponse(
            username = user.username,
            token = tokenService.createTokenForUser(user.id),
        )
    }

    fun login(request: AuthRequest): AuthResponse {
        val user = userRepository.findByUsername(request.username)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials") }

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }

        return AuthResponse(
            username = user.username,
            token = tokenService.createTokenForUser(user.id),
        )
    }
}
