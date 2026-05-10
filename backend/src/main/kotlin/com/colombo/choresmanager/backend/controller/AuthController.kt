package com.colombo.choresmanager.backend.controller

import com.colombo.choresmanager.backend.dto.AuthRequest
import com.colombo.choresmanager.backend.dto.AuthResponse
import com.colombo.choresmanager.backend.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: AuthRequest): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: AuthRequest): AuthResponse {
        return authService.login(request)
    }
}
