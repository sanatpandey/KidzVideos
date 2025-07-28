package com.kidztube.userservice.controller

import com.kidztube.userservice.domain.RefreshToken
import com.kidztube.userservice.domain.TokenRefreshRequest
import com.kidztube.userservice.domain.User
import com.kidztube.userservice.dtos.LoginDto
import com.kidztube.userservice.dtos.LoginResponseDto
import com.kidztube.userservice.dtos.RegisterDto
import com.kidztube.userservice.repo.RefreshTokenRepository
import com.kidztube.userservice.service.UserService
import com.kidztube.userservice.utils.JwtUtil
import com.kidztube.userservice.utils.TokenStore
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/user")
@AllArgsConstructor
class UserController {

    @Autowired
    private lateinit var  userService: UserService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var tokenStore: TokenStore

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @PostMapping("/register")
    public fun registerUser(@RequestBody dto: RegisterDto): ResponseEntity<Any> {
        val role = if (dto.email == "admin@gmail.com") "admin" else "user"
        val user = userService.register(dto, role)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/login")
    public fun login(@RequestBody loginDto: LoginDto,
                     response: HttpServletResponse): ResponseEntity<Any> {
        val user = userService.login(loginDto.email, loginDto.password)

        val cookie = Cookie("refreshToken", user.refreshToken)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = "/"
        cookie.maxAge = 7*24*60*60
        cookie.setAttribute("SameSite", "Strict")

        response.addCookie(cookie)

        return ResponseEntity.ok(
            mapOf(
                "accessToken" to user.accessToken,
                "userName" to user.userName,
                "role" to user.role
            )
        )
    }

    @GetMapping("/hello")
    public fun login(): ResponseEntity<String>{
        return ResponseEntity.ok("Hello Guys")
    }
}

/*@RestController
@RequestMapping("/hello")
class HelloController {

    @GetMapping
    fun sayHello(): String = "Hello from User Service"
}*/