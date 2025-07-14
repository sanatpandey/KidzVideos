package com.kidztube.content_service.service

import com.kidztube.content_service.domain.Video
import com.kidztube.content_service.dto.VideoDto
import com.kidztube.content_service.dto.VideoFileDto
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.Optional

interface VideoService {
    fun getVideo(id: Long): Optional<Video?>

    fun saveVideo(metaData: VideoFileDto,
                  videoKey: String): Video

    fun getAllVideoDetails(): List<VideoDto>
}