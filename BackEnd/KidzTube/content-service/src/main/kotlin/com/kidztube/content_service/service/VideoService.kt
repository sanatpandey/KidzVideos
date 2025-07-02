package com.kidztube.content_service.service

import com.kidztube.content_service.domain.Video
import com.kidztube.content_service.dto.VideoDto
import com.kidztube.content_service.dto.VideoFileDto
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

interface VideoService {
    fun getVideo(name: String, platform: String): Video

    fun saveVideo(metaData: VideoFileDto,
                  s3Url: String): Video

    fun getAllVideoDetails(): List<VideoDto>

    fun getAllVideoDetailsFromPlatform(platform: String): List<VideoDto>
}