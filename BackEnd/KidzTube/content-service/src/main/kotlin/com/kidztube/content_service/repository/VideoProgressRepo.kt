package com.kidztube.content_service.repository

import com.kidztube.content_service.domain.VideoProgress
import org.springframework.data.jpa.repository.JpaRepository

interface VideoProgressRepo: JpaRepository<VideoProgress, Long> {
    fun findByUserEmailAndVideoId(userEmail: String, videoId: Long): VideoProgress?
}