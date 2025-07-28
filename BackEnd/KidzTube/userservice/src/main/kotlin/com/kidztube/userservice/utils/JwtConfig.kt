package com.kidztube.userservice.utils

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
    var secret: String = "",
    var accessTokenExpiration: Long = 0,
    var refreshTokenExpiration: Long = 0
)