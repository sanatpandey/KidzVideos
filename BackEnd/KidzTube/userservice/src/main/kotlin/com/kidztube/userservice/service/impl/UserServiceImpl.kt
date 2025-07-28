package com.kidztube.userservice.service.impl

import com.kidztube.userservice.domain.RefreshToken
import com.kidztube.userservice.domain.User
import com.kidztube.userservice.dtos.LoginResponseDto
import com.kidztube.userservice.dtos.RegisterDto
import com.kidztube.userservice.dtos.UserDto
import com.kidztube.userservice.exceptions.UserAlreadyExistsException
import com.kidztube.userservice.exceptions.UserNotFoundException
import com.kidztube.userservice.repo.RefreshTokenRepository
import com.kidztube.userservice.repo.UserRepository
import com.kidztube.userservice.service.UserService
import com.kidztube.userservice.utils.JwtUtil
import jakarta.transaction.Transactional
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@AllArgsConstructor
open class UserServiceImpl(
    private val jwtUtil: JwtUtil,
    private val refreshTokenRepository: RefreshTokenRepository
): UserService {

    @Autowired
    private lateinit var repository: UserRepository

    override fun register(dto: RegisterDto, role: String): UserDto {
        val user = mapToUser(dto, role)

        if (repository.existsByEmail(user.email)) {
            throw UserAlreadyExistsException("User already exists.")
        }
        repository.save(user)
        return mapToUserDto(user)
    }

    @Transactional
    override fun login(email: String, password: String): LoginResponseDto {
        val user = authenticate(email, password)
        val accessToken = jwtUtil.generateAccessToken(user.email, user.role)
        val refreshToken = jwtUtil.generateRefreshToken(user.email)

        // Save refresh token
        refreshTokenRepository.deleteByUserEmail(user.email)
        refreshTokenRepository.save(
            RefreshToken(
                userEmail = user.email,
                token = refreshToken,
                expiryDate = LocalDateTime.now().plusDays(7)
            )
        )

        return LoginResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            email = email,
            userName = user.name,
            role = user.role
        )
    }

    override fun changePassword(email: String, oldPassword: String, newPassword: String) {
        val oldHashedPassword = oldPassword.hashCode()
        if (!repository.existsByEmail(email)) {
            throw UserNotFoundException("User not found.")
        }

        // check if old password is correct
        val user: User? = repository.findByEmail(email)
        if (user?.password !== oldHashedPassword.toString()) {
            throw UserNotFoundException("Incorrect Password")
        }
        val newHashedPassword = newPassword.hashCode()
        user.password = newHashedPassword.toString()
        repository.save(user)
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    private fun authenticate(email: String, password: String): User{
        val hashedPassword = password.hashCode()
        if (!repository.existsByEmail(email)) {
            throw UserNotFoundException("User not found.")
        }
        val user: User? = repository.findByEmail(email)
        if (user?.password != hashedPassword.toString()) {
            throw UserNotFoundException("User not found")
        }

        if(user.email.equals("admin@gmail.com")){
            user.role = "admin"
        }else{
            user.role = "user"
        }

        return user;
    }

    private fun mapToUser(dto: RegisterDto, role: String): User{
        val hashedPassword = dto.password.hashCode()
        return User(name = dto.name, email = dto.email, password = hashedPassword.toString(),
            ageGroup = dto.ageGroup, role = role)
    }

    private fun mapToUserDto(user: User): UserDto{
        return UserDto(
            name = user.name,
            userEmail = user.email,
            ageGroup = user.ageGroup,
            videoIds = user.video_ids
        )
    }

}