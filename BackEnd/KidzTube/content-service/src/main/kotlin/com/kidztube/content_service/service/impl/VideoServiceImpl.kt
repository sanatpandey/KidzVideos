package com.kidztube.content_service.service.impl

import com.kidztube.content_service.constants.platform
import com.kidztube.content_service.domain.Video
import com.kidztube.content_service.dto.VideoDto
import com.kidztube.content_service.dto.VideoFileDto
import com.kidztube.content_service.exceptions.VideoAlreadyExistsException
import com.kidztube.content_service.exceptions.VideoNotFoundException
import com.kidztube.content_service.repository.VideoRepo
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
class VideoServiceImpl: VideoService {

    @Autowired
    private lateinit var videoRepo: VideoRepo

    val external_provider_path: String = "/home/sanat/kidzVideos/"

    override fun getVideo(name: String, platform: String): Video {
            if (!videoRepo.existsByName(name)){
                throw VideoNotFoundException()
            }
            return videoRepo.findByName(name)
    }

    override fun saveVideo(
        metaData: VideoFileDto,
        s3Url: String
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
            s3Url = s3Url)
        videoRepo.save(video)
        return video
    }

    /*override fun saveVideo(
        name: String,

    ): Video {
        if (videoRepo.existsByName(name)){
            throw VideoAlreadyExistsException()
        }
        val video = Video(name= name, data = file.bytes)
        videoRepo.save(video)
        return video
    }*/

    override fun getAllVideoDetails(): List<VideoDto> {

        // return repo.getAllEntryNames();
        val videoDetails: MutableList<VideoDto?> = ArrayList<VideoDto?>()
        for (name in videoRepo.getAllEntryNames()!!)
            name?.let { videoDetails.add(VideoDto(name = it, "Local")) }
        for (p in platform.values()) for (file in File(external_provider_path + p.videoName).listFiles()) {
            val video = VideoDto(file.getName(), p.videoName)
            videoDetails.add(video)
        }
        return videoDetails as List<VideoDto>
    }

    override fun getAllVideoDetailsFromPlatform(pform: String): List<VideoDto> {

        // return repo.getAllEntryNames();
        val videoDetails: MutableList<VideoDto?> = ArrayList<VideoDto?>()
        if (pform == "Local") {
            for (name in videoRepo.getAllEntryNames()!!)
                name?.let { videoDetails.add(VideoDto(it, "Local")) }
            return videoDetails as List<VideoDto>
        } else {
            for (p in platform.values())
                if (p.videoName.equals(pform)) {
                    for (file in File(external_provider_path + p.videoName).listFiles()) {
                        val video = VideoDto(file.getName(), p.videoName)
                        videoDetails.add(video)
                    }
                 break
            }
            return videoDetails as List<VideoDto>
        }
    }
}