package com.kidztube.userservice.dtos

data class UserDto(
    val name: String,
    val userEmail: String,
    val ageGroup: String,
    val videoIds: String
)
