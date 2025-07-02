package com.kidztube.userservice.utils

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class TokenStore {
    private val tokenMap = ConcurrentHashMap<String, String>()

    fun storeToken(username: String, token: String) {
        tokenMap[username] = token
    }

    fun getToken(username: String): String? {
        return tokenMap[username]
    }
}