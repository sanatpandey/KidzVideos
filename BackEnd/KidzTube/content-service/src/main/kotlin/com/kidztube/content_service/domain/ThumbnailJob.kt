package com.kidztube.content_service.domain

data class ThumbnailJob(
    val videoUrl: String,
    val s3Key: String,
    val videoId: Long = 0
)
