package com.kidztube.content_service.service

import com.kidztube.content_service.domain.ThumbnailJob
import com.kidztube.content_service.utils.AwsProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

@Service
class ThumbnailService(
    private val s3Client: S3Client,
    private val awsProperties: AwsProperties
) {
    fun generateAndUploadThumbnail(job: ThumbnailJob): String{
        val tempVideo = File("/Users/sanatpandey/Downloads/temp_video.mp4")
        val thumbnailFile = File("/Users/sanatpandey/Downloads/temp_thumb.jpg")

        try {
            s3Client.getObject(
                GetObjectRequest.builder()
                    .bucket(awsProperties.bucket)
                    .key(job.s3Key)
                    .build(), ResponseTransformer.toFile(tempVideo.toPath())
            )


            //Generate Thumbnail
            val command = listOf(
                "ffmpeg",
                "-i",
                tempVideo.absolutePath,
                "-ss",
                "00:00:01",
                "-vframes",
                "1",
                thumbnailFile.absolutePath
            )
            val process = ProcessBuilder(command).redirectErrorStream(true).start()

            process.inputStream.bufferedReader().use { it.readText() }

            process.waitFor()

            // Upload to S3

            val thumbKey = "thumbnails/${job.videoId}.jpg"
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(awsProperties.bucket)
                    .key(thumbKey)
                    .contentType("image/jpeg")
                    .build(), RequestBody.fromFile(thumbnailFile)
            )

            return thumbKey
        }finally {
            if (tempVideo.exists()) tempVideo.delete()
            if (thumbnailFile.exists()) thumbnailFile.delete()
        }
    }
}