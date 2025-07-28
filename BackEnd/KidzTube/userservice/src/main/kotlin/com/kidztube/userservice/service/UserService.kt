package com.kidztube.userservice.service

import com.kidztube.userservice.domain.User
import com.kidztube.userservice.dtos.LoginResponseDto
import com.kidztube.userservice.dtos.RegisterDto
import com.kidztube.userservice.dtos.UserDto

interface UserService {
    fun register(dto: RegisterDto, role: String): UserDto
    fun login(email: String, password: String): LoginResponseDto
    fun changePassword(email: String, oldPassword: String, newPassword: String)
    fun logout()
}