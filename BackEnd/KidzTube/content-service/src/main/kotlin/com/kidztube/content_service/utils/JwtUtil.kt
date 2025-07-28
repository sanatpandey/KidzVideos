package com.kidztube.content_service.utils

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component

@Component
class JwtUtil(
    private val jwtConfig: JwtConfig
) {
    fun extractEmail(token: String): String{
        val claims = Jwts.parserBuilder()
            .setSigningKey(jwtConfig.secret.toByteArray())
            .build()
            .parseClaimsJws(token.removePrefix("Bearer "))
        return claims.body.subject
    }

    fun validateToken(token: String): Boolean{
        return try {
            Jwts.parserBuilder()
                .setSigningKey(jwtConfig.secret.toByteArray())
                .build()
                .parseClaimsJws(token.removePrefix("Bearer "))

            true
        }catch (e: Exception){
            System.out.println(e.toString())
            false
        }
    }
}