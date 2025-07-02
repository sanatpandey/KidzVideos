package com.kidztube.content_service.dto

data class VideoFileDto(
    val name: String,
    val genre: String,
    val isTrending: Boolean,
    val ageGroup: String,
    val description: String
)
