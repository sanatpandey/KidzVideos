package com.kidztube.content_service.utils

import com.kidztube.content_service.domain.ThumbnailJob
import com.kidztube.content_service.service.ThumbnailService
import com.kidztube.content_service.service.VideoMetadataService
import org.springframework.stereotype.Component
import org.springframework.kafka.annotation.KafkaListener

@Component
class ThumbnailJobConsumer(
    private val thumbnailService: ThumbnailService,
    private val videoMetadataService: VideoMetadataService
) {

    @KafkaListener(topics = ["thumbnail-requests"], groupId = "thumbnail-generator")
    fun consume(job: ThumbnailJob){
        println("Received Job:$job")
        try {
            val thumbnailKey = thumbnailService.generateAndUploadThumbnail(job)

            videoMetadataService.updateThumbnailUrl(videoId = job.videoId, thumbnailKey = thumbnailKey)
            println("Thumbnail generated for ${job.videoId}")
        }catch (e: Exception){
            println(e.message)
        }
    }
}