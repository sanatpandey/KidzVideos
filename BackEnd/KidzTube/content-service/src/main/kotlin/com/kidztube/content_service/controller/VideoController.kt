package com.kidztube.content_service.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kidztube.content_service.component.ThumbnailWorker
import com.kidztube.content_service.domain.ThumbnailJob
import com.kidztube.content_service.dto.VideoFileDto
import com.kidztube.content_service.service.S3Service
import com.kidztube.content_service.service.VideoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequestMapping("video")
class VideoController(
    private val kafkaTemplate: KafkaTemplate<String, ThumbnailJob>
) {

    @Autowired
    private lateinit var service: VideoService

    @Autowired
    private lateinit var s3Service: S3Service

    @Autowired
    private lateinit var thumbnailWorker: ThumbnailWorker

    @CrossOrigin(origins = ["http://localhost:5173"])
    @PostMapping("/save", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun saveVideo(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("data") data: VideoFileDto
    ): ResponseEntity<String?> {
        val s3Key = "videos/${file.originalFilename}"
        val url = s3Service.uploadFile(file)
        val saved = service.saveVideo(
            metaData = data,
            s3Url = url)

        kafkaTemplate.send("thumbnail-requests", ThumbnailJob(
            videoUrl = url,
            s3Key = s3Key,
            videoId = saved.id))

        return ResponseEntity.ok<String?>("Video uploaded successfully, thumbnail will be generated")
    }
}