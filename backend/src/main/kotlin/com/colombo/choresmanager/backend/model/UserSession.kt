package com.colombo.choresmanager.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "user_sessions")
class UserSession(
    @Id
    var token: String = "",

    @Column(nullable = false)
    var userId: Long = 0,

    @Column(nullable = false)
    var expiresAt: ZonedDateTime = ZonedDateTime.now().plusDays(30),
)
