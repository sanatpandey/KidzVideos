package com.kidztube.userservice.repo

import com.kidztube.userservice.domain.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying

interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?
    @Modifying
    fun deleteByUserEmail(email: String)
}