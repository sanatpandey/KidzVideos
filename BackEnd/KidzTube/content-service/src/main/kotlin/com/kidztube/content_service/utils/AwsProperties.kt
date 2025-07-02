package com.kidztube.content_service.utils

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "aws")
data class AwsProperties(
    var region: String = "",
    var bucket: String = "",
    var accessKey: String = "",
    var secret: String = ""
)
