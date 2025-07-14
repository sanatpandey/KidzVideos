package com.kidztube.content_service.utils

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    private val jwtConfig: JwtConfig
) {
    fun extractEmail(token: String): String{
        val claims = Jwts.parser()
            .setSigningKey(jwtConfig.secret.toByteArray())
            .parseClaimsJws(token.removePrefix("Bearer "))
            .body

        return claims.subject
    }

    fun validateToken(token: String): Boolean{
        return try {
            Jwts.parser()
                .setSigningKey(jwtConfig.secret.toByteArray())
                .parseClaimsJws(token.removePrefix("Bearer "))

            true
        }catch (e: Exception){
            false
        }
    }
}