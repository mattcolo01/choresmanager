package com.colombo.choresmanager.backend.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "chores")
class RemoteChore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var intervalDays: Int = 1,

    @Column(nullable = false)
    var lastDoneAt: ZonedDateTime = ZonedDateTime.now(),

    @Column(nullable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var owner: AppUser? = null,
)
