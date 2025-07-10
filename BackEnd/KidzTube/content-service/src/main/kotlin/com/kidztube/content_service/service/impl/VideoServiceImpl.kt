package com.kidztube.content_service.service.impl

import com.kidztube.content_service.constants.platform
import com.kidztube.content_service.domain.Video
import com.kidztube.content_service.dto.VideoDto
import com.kidztube.content_service.dto.VideoFileDto
import com.kidztube.content_service.exceptions.VideoAlreadyExistsException
import com.kidztube.content_service.exceptions.VideoNotFoundException
import com.kidztube.content_service.repository.VideoRepo
import com.kidztube.content_service.service.S3Service
import com.kidztube.content_service.service.VideoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.time.LocalDate
import java.util.Date

@Service
class VideoServiceImpl(
    private val s3Service: S3Service
): VideoService {

    @Autowired
    private lateinit var videoRepo: VideoRepo

    override fun getVideo(name: String, platform: String): Video {
            if (!videoRepo.existsByName(name)){
                throw VideoNotFoundException()
            }
            return videoRepo.findByName(name)
    }

    override fun saveVideo(
        metaData: VideoFileDto,
        videoKey: String
    ): Video {
        if (videoRepo.existsByName(metaData.name)){
            throw VideoAlreadyExistsException()
        }
        val video = Video(
            name= metaData.name,
            genere = metaData.genre,
            isTrending = metaData.isTrending.toString(),
            columnLastChanged = LocalDate.now().toString(),
            thumbnailName = metaData.name,
            description = metaData.description,
            ageGroup = metaData.ageGroup,
            videoKey = videoKey)
        videoRepo.save(video)
        return video
    }

    override fun getAllVideoDetails(): List<VideoDto> {
        return videoRepo.findAll().map {
            VideoDto(
                name = it.name,
                description = it.description,
                rating = 0f,
                genre = it.genere,
                thumbnailUrl = it.thumbnailKey?.let { key -> s3Service.generatePresignedUrl(key) }.toString()
            )
        }
    }
}