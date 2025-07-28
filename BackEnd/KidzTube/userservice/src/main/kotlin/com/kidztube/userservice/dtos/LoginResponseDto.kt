package com.kidztube.userservice.dtos

data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val email: String,
    val userName: String,
    val role: String
)
