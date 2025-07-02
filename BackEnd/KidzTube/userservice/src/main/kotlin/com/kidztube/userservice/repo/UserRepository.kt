package com.kidztube.userservice.repo

import com.kidztube.userservice.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long>{
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User
}