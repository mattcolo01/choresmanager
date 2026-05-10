package com.colombo.choresmanager.backend.dto

import jakarta.validation.constraints.NotBlank

data class AuthRequest(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String,
)

data class AuthResponse(
    val username: String,
    val token: String,
)
