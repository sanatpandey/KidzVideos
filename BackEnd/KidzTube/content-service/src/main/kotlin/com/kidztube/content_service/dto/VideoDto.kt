package com.kidztube.content_service.dto

data class VideoDto(
    val name: String,
    val description: String,
    val rating: Float,
    val thumbnailUrl: String,
    val genre: String
)
