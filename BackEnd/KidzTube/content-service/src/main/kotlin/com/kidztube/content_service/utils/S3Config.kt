package com.kidztube.content_service.utils

import lombok.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config(
    private val awsProperties: AwsProperties
) {
    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .region(Region.of("eu-north-1"))
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
    }
}