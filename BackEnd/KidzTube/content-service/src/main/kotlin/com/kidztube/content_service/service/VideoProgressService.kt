package com.kidztube.content_service.service

import com.kidztube.content_service.domain.VideoProgress
import com.kidztube.content_service.repository.VideoProgressRepo
import com.kidztube.content_service.repository.VideoRepo
import com.kidztube.content_service.utils.JwtUtil
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class VideoProgressService (
    private val progressRepo: VideoProgressRepo,
    private val videoRepo: VideoRepo,
    private val jwtUtil: JwtUtil
){
    fun saveOrUpdateProgress(token: String, videoId: Long, seconds: Int){
        if (!jwtUtil.validateToken(token)) {
            return
        }
        val userEmail = jwtUtil.extractEmail(token)
        val video = videoRepo.findById(videoId).orElseThrow{ RuntimeException("Video Not found") }

        val existing = progressRepo.findByUserEmailAndVideoId(userEmail, videoId)

        if(existing != null){
            existing.lastWatchedSecond = seconds
            existing.updatedAt = LocalDateTime.now()
            progressRepo.save(existing)
        }else{
            val progress = VideoProgress(
                userEmail = userEmail,
                video = video,
                lastWatchedSecond = seconds,
                updatedAt = LocalDateTime.now()
            )
            progressRepo.save(progress)
        }
    }

    fun getProgress(token: String, videoId: Long): Int{
        if (!jwtUtil.validateToken(token)) {
            return 0
        }
        val userEmail = jwtUtil.extractEmail(token)
        return progressRepo.findByUserEmailAndVideoId(
            userEmail, videoId
        )?.lastWatchedSecond ?: 0
    }
}