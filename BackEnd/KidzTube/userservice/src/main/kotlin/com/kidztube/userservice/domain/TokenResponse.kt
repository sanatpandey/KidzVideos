package com.kidztube.userservice.domain

data class TokenResponse(
    val accessToken: String,
    val refreshAccessToken: String
)
