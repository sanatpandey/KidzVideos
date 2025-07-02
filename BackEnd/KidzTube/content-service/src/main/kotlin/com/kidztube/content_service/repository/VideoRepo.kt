package com.kidztube.content_service.repository

import com.kidztube.content_service.domain.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VideoRepo: JpaRepository<Video, Long> {
    fun findByName(name: String): Video
    fun existsByName(name: String): Boolean

    @Query(nativeQuery = true, value = "SELECT name FROM video")
    fun getAllEntryNames(): MutableList<String?>?
}