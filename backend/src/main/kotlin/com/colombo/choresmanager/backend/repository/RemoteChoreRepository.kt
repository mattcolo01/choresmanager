package com.colombo.choresmanager.backend.repository

import com.colombo.choresmanager.backend.model.RemoteChore
import org.springframework.data.jpa.repository.JpaRepository

interface RemoteChoreRepository : JpaRepository<RemoteChore, Long> {
    fun findAllByOwnerIdOrderByCreatedAtAsc(ownerId: Long): List<RemoteChore>
    fun findByIdAndOwnerId(choreId: Long, ownerId: Long): RemoteChore?
}
