package com.kidztube.content_service.repository

import com.kidztube.content_service.domain.VideoProgress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface VideoProgressRepo: JpaRepository<VideoProgress, Long> {
    fun findByUserEmailAndVideoId(userEmail: String, videoId: Long): VideoProgress?

    @Modifying
    @Query("UPDATE VideoProgress vp SET vp.lastWatchedSecond = :seconds WHERE vp.userEmail = :userEmail AND vp.video.id = :videoId")
    fun updateSecondsIfExists(
        @Param("userEmail") userEmail: String,
        @Param("videoId") videoId: Long,
        @Param("seconds") seconds: Int
    ): Int
}