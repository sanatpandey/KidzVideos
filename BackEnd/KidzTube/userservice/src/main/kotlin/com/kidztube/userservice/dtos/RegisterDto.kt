package com.kidztube.userservice.dtos

data class RegisterDto(
    val name: String,
    val email: String,
    val password: String,
    val ageGroup: String
)
