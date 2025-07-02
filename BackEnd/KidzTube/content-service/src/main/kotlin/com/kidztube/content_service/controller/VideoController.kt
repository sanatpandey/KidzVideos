package com.kidztube.content_service.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kidztube.content_service.dto.VideoFileDto
import com.kidztube.content_service.service.S3Service
import com.kidztube.content_service.service.VideoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@RestController
@RequestMapping("video")
class VideoController {

    @Autowired
    private lateinit var service: VideoService

    @Autowired
    private lateinit var s3Service: S3Service

    @PostMapping("/save", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun saveVideo(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("data") data: VideoFileDto
    ): ResponseEntity<String?> {
        val url = s3Service.uploadFile(file)
        service.saveVideo(
            metaData = data,
            s3Url = url)
        return ResponseEntity.ok<String?>("Video saved successfully.")
    }

    /*@PostMapping("/save", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun saveVideo(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("data") data: String // TEMP: receive as raw JSON string
    ): ResponseEntity<String> {
        println("File received: ${file.originalFilename}")
        println("Raw JSON received: $data")

        // OPTIONAL: parse manually using ObjectMapper
        val objectMapper = jacksonObjectMapper()
        val parsed = objectMapper.readValue(data, VideoFileDto::class.java)
        println("Parsed DTO: $parsed")

        return ResponseEntity.ok("Video saved successfully!")
    }*/
}