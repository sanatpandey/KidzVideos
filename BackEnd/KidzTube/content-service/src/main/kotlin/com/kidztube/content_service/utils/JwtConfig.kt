package com.kidztube.content_service.utils

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
    var secret: String = "",
    var expiration: Long = 0
)