package com.kidztube.content_service.service

import com.kidztube.content_service.utils.AwsProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
class S3Service(
    private val s3Client: S3Client,
    private val awsProperties: AwsProperties
) {
    fun uploadFile(file: MultipartFile): String{
        val key = "videos/${file.originalFilename}"

        val putRequest = PutObjectRequest.builder()
            .bucket(awsProperties.bucket)
            .key(key)
            .contentType(file.contentType)
            .build()

        s3Client.putObject(
            putRequest,
            RequestBody.fromBytes(file.bytes)
        )

        return "https://${awsProperties.bucket}.s3.amazonaws.com/${awsProperties.accessKey}"
    }
}