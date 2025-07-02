package com.kidztube.userservice.controller

import com.kidztube.userservice.domain.User
import com.kidztube.userservice.dtos.LoginDto
import com.kidztube.userservice.dtos.RegisterDto
import com.kidztube.userservice.service.UserService
import com.kidztube.userservice.utils.JwtUtil
import com.kidztube.userservice.utils.TokenStore
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

@RestController
@RequestMapping("user")
@AllArgsConstructor
class UserController {

    @Autowired
    private lateinit var  userService: UserService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var tokenStore: TokenStore

    @PostMapping("/register")
    public fun registerUser(@RequestBody dto: RegisterDto): ResponseEntity<Map<String, Any>?> {
        val role = if (dto.email == "admin@gmail.com") "admin" else "user"
        val user = userService.register(dto, role)

        val existingToken = tokenStore.getToken(user.email)
        if (existingToken != null) {
            return ResponseEntity.ok(mapOf("token" to existingToken))
        }
        val token = jwtUtil.generateToken(user.email, role)
        tokenStore.storeToken(user.email, token)
        return ResponseEntity.ok(mapOf("user" to user, "token" to token))
    }

    @PostMapping("/login")
    public fun login(@RequestBody loginDto: LoginDto): ResponseEntity<Map<String, String>?> {
        val email = loginDto.email
        val password = loginDto.password
        val user = userService.login(email, password)

        val token = tokenStore.getToken(email)
        if (token != null) {
            return ResponseEntity.ok(mapOf("token" to token, "userName" to email, "role" to user.role))
        } else {
            // fallback (optional): generate and store new token
            val newToken = jwtUtil.generateToken(email, user.role)
            tokenStore.storeToken(email, newToken)
            return ResponseEntity.ok(mapOf("token" to newToken, "userName" to email, "role" to user.role))
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "Invalid credentials"))
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
