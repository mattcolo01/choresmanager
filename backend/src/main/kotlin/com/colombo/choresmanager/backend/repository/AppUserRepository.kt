package com.colombo.choresmanager.backend.repository

import com.colombo.choresmanager.backend.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface AppUserRepository : JpaRepository<AppUser, Long> {
    fun findByUsername(username: String): Optional<AppUser>
}
