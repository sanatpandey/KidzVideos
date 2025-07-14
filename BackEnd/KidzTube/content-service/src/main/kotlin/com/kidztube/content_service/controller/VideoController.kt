package com.kidztube.content_service.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kidztube.content_service.component.ThumbnailWorker
import com.kidztube.content_service.domain.ThumbnailJob
import com.kidztube.content_service.domain.Video
import com.kidztube.content_service.dto.VideoDto
import com.kidztube.content_service.dto.VideoFileDto
import com.kidztube.content_service.exceptions.VideoNotFoundException
import com.kidztube.content_service.service.S3Service
import com.kidztube.content_service.service.VideoService
import com.kidztube.content_service.utils.AwsProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.ResponseEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import java.io.IOException
import java.io.InputStream

@RestController
@RequestMapping("video")
class VideoController(
    private val kafkaTemplate: KafkaTemplate<String, ThumbnailJob>,
    private val s3Client: S3Client,
    private val awsProperties: AwsProperties
) {

    @Autowired
    private lateinit var service: VideoService

    @Autowired
    private lateinit var s3Service: S3Service

    @CrossOrigin(origins = ["http://localhost:5173"])
    @PostMapping("/save", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun saveVideo(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("data") data: VideoFileDto
    ): ResponseEntity<String?> {
        val s3Key = "videos/${file.originalFilename}"
        val url = s3Service.uploadFile(file, s3Key)
        val saved = service.saveVideo(
            metaData = data,
            videoKey = s3Key)

        kafkaTemplate.send("thumbnail-requests", ThumbnailJob(
            videoUrl = url,
            s3Key = s3Key,
            videoId = saved.id))

        return ResponseEntity.ok<String?>("Video uploaded successfully, thumbnail will be generated")
    }

    @GetMapping("/all")
    fun getAllVideos(): List<VideoDto> = service.getAllVideoDetails()

    @GetMapping("/stream/{id}")
    fun streamVideo(
        @PathVariable id: Long,
        @RequestHeader(value = "Range", required = false) rangeHeader: String?
    ): ResponseEntity<StreamingResponseBody>{
        if (service.getVideo(id).isPresent) {
            val videoKey = service.getVideo(id).get().videoKey
            val objectHead = s3Client.headObject { it.bucket(awsProperties.bucket).key(videoKey) }

            val totalSize = objectHead.contentLength()

            val (start, end) = parseRangeHeader(rangeHeader, totalSize)

            val getObjectRequest = GetObjectRequest.builder()
                .bucket(awsProperties.bucket)
                .key(videoKey)
                .range("bytes=$start - $end")
                .build()

            val s3Object: InputStream = s3Client.getObject(getObjectRequest)

            val headers = HttpHeaders()
            //headers.contentType = MediaTypeFactory.getMediaType(videoKey).orElse(MediaType.APPLICATION_OCTET_STREAM)
            headers.contentType = MediaType("video", "mp4")
            headers["Access-Control-Allow-Origin"] = "*"
            headers["Accept-Ranges"] = "bytes"
            headers.contentLength = end - start + 1
            headers["Content-Range"] = "bytes $start - $end/$totalSize"

            val response = StreamingResponseBody { outputStream ->
                s3Object.use { input ->
                    input.copyTo(outputStream)
                }
            }

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).body(response)
        }else{
            throw VideoNotFoundException()
        }
    }

    private fun parseRangeHeader(rangeHeader: String?, fileSize: Long): Pair<Long, Long>{
        if(rangeHeader == null || !rangeHeader.startsWith("bytes=")){
            return 0L to fileSize -1
        }

        val range = rangeHeader.removePrefix("bytes=").split("-")
        val start = range[0].toLongOrNull() ?: 0L
        val end = range.getOrNull(1)?.toLongOrNull() ?: (fileSize - 1)

        return start to minOf(end, fileSize - 1)
    }
}