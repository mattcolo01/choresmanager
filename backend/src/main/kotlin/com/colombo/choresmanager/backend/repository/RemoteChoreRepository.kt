package com.colombo.choresmanager.backend.repository

import com.colombo.choresmanager.backend.model.RemoteChore
import org.springframework.data.jpa.repository.JpaRepository

interface RemoteChoreRepository : JpaRepository<RemoteChore, Long> {
    fun findAllByOwner_IdOrderByCreatedAtAsc(ownerId: Long): List<RemoteChore>
    fun findByIdAndOwner_Id(choreId: Long, ownerId: Long): RemoteChore?
}
