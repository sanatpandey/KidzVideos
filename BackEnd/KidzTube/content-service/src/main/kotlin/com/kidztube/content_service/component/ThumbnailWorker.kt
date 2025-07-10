package com.kidztube.content_service.component

import com.kidztube.content_service.domain.ThumbnailJob
import com.kidztube.content_service.repository.VideoRepo
import com.kidztube.content_service.service.ThumbnailService
import com.kidztube.content_service.utils.AwsProperties
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

@Component
class ThumbnailWorker(
    private val thumnailService: ThumbnailService,
    private val videoRepo: VideoRepo,
    private val awsProperties: AwsProperties
) {

    private val executor = Executors.newSingleThreadExecutor()

    fun submit(job: ThumbnailJob){
        executor.submit {
            try {
                val thumbKey = thumnailService.generateAndUploadThumbnail(job)
                val video = videoRepo.findById(job.videoId).orElseThrow()
                video.thumbnailKey = thumbKey
                videoRepo.save(video)
                println("Thumbnail generated and DB updated for video id ${video.id}")
            }catch (e: Exception){
                println("Failed to generate thumbnail: ${e.message}")
            }
        }
    }
}