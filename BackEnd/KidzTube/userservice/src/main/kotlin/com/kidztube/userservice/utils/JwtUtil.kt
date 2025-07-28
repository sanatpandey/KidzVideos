package com.kidztube.userservice.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import lombok.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.Date

@Component
class JwtUtil(
    private val jwtConfig: JwtConfig
) {
    fun generateAccessToken(userName: String, role: String): String {
        val now = Date()
        val expiry = Date(now.time + jwtConfig.accessTokenExpiration)
        val claims = mapOf("role" to role)
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userName)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray()), SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(jwtConfig.secret.toByteArray())
                .build()
                .parseClaimsJws(token)
            true
        }catch (e: Exception){
            false
        }
    }

    fun getUserNameFromToken(token: String): String{
        val claims = Jwts.parserBuilder()
            .setSigningKey(jwtConfig.secret.toByteArray())
            .build()
            .parseClaimsJws(token)
        return claims.body.subject
    }

    fun extractRole(token: String): String? {
        return extractAllClaims(token)["role"] as? String
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(jwtConfig.secret.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun generateRefreshToken(email: String): String = Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtConfig.refreshTokenExpiration))
            .signWith(Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray()), SignatureAlgorithm.HS256)
            .compact()

    fun getRefreshTokenExpiryDate(): LocalDateTime {
        return LocalDateTime.now().plus(jwtConfig.refreshTokenExpiration, ChronoUnit.MILLIS)
    }
}