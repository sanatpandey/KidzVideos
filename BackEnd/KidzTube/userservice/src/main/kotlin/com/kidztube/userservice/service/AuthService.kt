package com.kidztube.userservice.service

import com.kidztube.userservice.domain.RefreshToken
import com.kidztube.userservice.domain.TokenRefreshRequest
import com.kidztube.userservice.domain.TokenResponse
import com.kidztube.userservice.repo.RefreshTokenRepository
import com.kidztube.userservice.repo.UserRepository
import com.kidztube.userservice.utils.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    val userRepository: UserRepository,
    val refreshTokenRepository: RefreshTokenRepository,
    val jwtUtil: JwtUtil
) {

    fun refreshToken(request: HttpServletRequest): ResponseEntity<Any>{
        val cookie = request.cookies?.firstOrNull { it.name == "refreshToken" }
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "No refresh token"))

        val savedToken = refreshTokenRepository.findByToken(cookie.value)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                mapOf(
                    "error" to "Invalid Token"
                )
            )

        if (savedToken.expiryDate.isBefore(LocalDateTime.now())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                mapOf(
                    "error" to "Invalid Token"
                )
            )
        }

        if (savedToken == null || !jwtUtil.validateToken(cookie.value)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                "Invalid or expired refresh token"
            )
        }

        val userEmail = jwtUtil.getUserNameFromToken(savedToken.token)
        val newAccessToken = jwtUtil.generateAccessToken(userEmail, "USER")
        val newRefreshToken = jwtUtil.generateRefreshToken(userEmail)

        refreshTokenRepository.delete(savedToken)
        refreshTokenRepository.save(
            RefreshToken(
                token = newRefreshToken,
                userEmail = userEmail,
                expiryDate = jwtUtil.getRefreshTokenExpiryDate()
            )
        )

        return ResponseEntity.ok(
            mapOf(
                "accessToken" to newAccessToken
            )
        )
    }
}