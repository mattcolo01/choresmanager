package com.colombo.choresmanager.backend.controller

import com.colombo.choresmanager.backend.dto.ChoreResponse
import com.colombo.choresmanager.backend.dto.CompleteChoreRequest
import com.colombo.choresmanager.backend.dto.CreateChoreRequest
import com.colombo.choresmanager.backend.dto.toResponse
import com.colombo.choresmanager.backend.model.RemoteChore
import com.colombo.choresmanager.backend.repository.AppUserRepository
import com.colombo.choresmanager.backend.repository.RemoteChoreRepository
import com.colombo.choresmanager.backend.security.UserPrincipal
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.ZonedDateTime

@RestController
@RequestMapping("/api/chores")
class ChoreController(
    private val choreRepository: RemoteChoreRepository,
    private val userRepository: AppUserRepository,
) {
    @GetMapping
    fun list(@AuthenticationPrincipal principal: UserPrincipal): List<ChoreResponse> {
        return choreRepository.findAllByOwnerIdOrderByCreatedAtAsc(principal.userId)
            .map { it.toResponse() }
    }

    @PostMapping
    fun create(
        @AuthenticationPrincipal principal: UserPrincipal,
        @Valid @RequestBody request: CreateChoreRequest,
    ): ChoreResponse {
        val owner = userRepository.findById(principal.userId)
            .orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user") }

        return choreRepository.save(
            RemoteChore(
                name = request.name,
                intervalDays = request.intervalDays,
                lastDoneAt = request.lastDoneAt,
                createdAt = ZonedDateTime.now(),
                owner = owner,
            ),
        ).toResponse()
    }

    @PostMapping("/{id}/complete")
    fun complete(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: CompleteChoreRequest,
    ): ChoreResponse {
        val chore = choreRepository.findByIdAndOwnerId(id, principal.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chore not found")
        chore.lastDoneAt = request.completedAt
        return choreRepository.save(chore).toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal principal: UserPrincipal,
        @PathVariable id: Long,
    ) {
        val chore = choreRepository.findByIdAndOwnerId(id, principal.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chore not found")
        choreRepository.delete(chore)
    }
}
