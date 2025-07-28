package com.kidztube.userservice.controller

import com.kidztube.userservice.domain.TokenRefreshRequest
import com.kidztube.userservice.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/refresh-token")
    fun refreshToken(
        request: HttpServletRequest
    ): ResponseEntity<Any>{
        return authService.refreshToken(request)
    }
}