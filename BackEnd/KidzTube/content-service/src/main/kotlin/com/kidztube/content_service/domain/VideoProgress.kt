package com.kidztube.content_service.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(name = "video_progress", uniqueConstraints = [UniqueConstraint(columnNames = ["user_email", "video_id"])])
data class VideoProgress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_email", nullable = false)
    val userEmail: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    val video: Video,

    @Column(name = "last_watched_second", nullable = false)
    var lastWatchedSecond: Int = 0,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
