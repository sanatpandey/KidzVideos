package com.kidztube.content_service.controller

import com.kidztube.content_service.service.VideoProgressService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/video/progress")
class VideoProgressController(
    private val progressService: VideoProgressService
) {

    @PostMapping("/save")
    fun saveProgress(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam videoId: Long,
        @RequestParam seconds: Int
        ){
            progressService.saveOrUpdateProgress(authorizationHeader, videoId, seconds)
    }

    @GetMapping("/get")
    fun getProgress(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam videoId: Long
    ): Int {
        return progressService.getProgress(authorizationHeader, videoId)
    }
}