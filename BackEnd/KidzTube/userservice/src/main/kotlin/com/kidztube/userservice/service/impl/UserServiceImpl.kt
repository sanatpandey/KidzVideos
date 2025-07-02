package com.kidztube.userservice.service.impl

import com.kidztube.userservice.domain.User
import com.kidztube.userservice.dtos.RegisterDto
import com.kidztube.userservice.exceptions.UserAlreadyExistsException
import com.kidztube.userservice.exceptions.UserNotFoundException
import com.kidztube.userservice.repo.UserRepository
import com.kidztube.userservice.service.UserService
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@AllArgsConstructor
open class UserServiceImpl: UserService {

    @Autowired
    private lateinit var repository: UserRepository

    override fun register(dto: RegisterDto, role: String): User {
        val user = mapToUser(dto, role)

        if (repository.existsByEmail(user.email)) {
            throw UserAlreadyExistsException("User already exists.")
        }
        repository.save(user)
        return user
    }

    override fun login(email: String, password: String): User {
        val hashedPassword = password.hashCode()
        if (!repository.existsByEmail(email)) {
            throw UserNotFoundException("User not found.")
        }
        val user: User? = repository.findByEmail(email)
        if (user?.password != hashedPassword.toString()) {
            throw UserNotFoundException("User not found")
        }

        return user;
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

    private fun mapToUser(dto: RegisterDto, role: String): User{
        val hashedPassword = dto.password.hashCode()
        return User(name = dto.name, email = dto.email, password = hashedPassword.toString(),
            ageGroup = dto.ageGroup, role = role)
    }

}