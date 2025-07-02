package com.kidztube.userservice.service

import com.kidztube.userservice.domain.User
import com.kidztube.userservice.dtos.RegisterDto

interface UserService {
    fun register(dto: RegisterDto, role: String): User
    fun login(email: String, password: String): User
    fun changePassword(email: String, oldPassword: String, newPassword: String)
    fun logout()
}