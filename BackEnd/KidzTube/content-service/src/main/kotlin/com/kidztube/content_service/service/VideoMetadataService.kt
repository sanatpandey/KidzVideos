package com.kidztube.content_service.service

import com.kidztube.content_service.repository.VideoRepo
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class VideoMetadataService(
    private val videoRepo: VideoRepo
) {
    fun updateThumbnailUrl(videoId: Long, thumbnailKey: String){
        val video = videoRepo.findById(videoId).orElseThrow{ RuntimeException("Video Not found") }

        video.thumbnailKey = thumbnailKey
        videoRepo.save(video)
    }
}