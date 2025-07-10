package com.kidztube.content_service.service

import com.kidztube.content_service.utils.AwsProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration

@Service
class S3Service(
    private val s3Client: S3Client,
    private val awsProperties: AwsProperties
) {
    private val s3Presigner: S3Presigner = S3Presigner.builder()
        .region(Region.EU_NORTH_1) // Replace with your actual region
        .credentialsProvider(DefaultCredentialsProvider.create())
        .build()
    fun uploadFile(file: MultipartFile, key: String): String{
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

    fun generatePresignedUrl(key: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(awsProperties.bucket)
            .key(key)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(60))
            .getObjectRequest(getObjectRequest)
            .build()

        val presigned = s3Presigner.presignGetObject(presignRequest)
        return presigned.url().toString()
    }
}