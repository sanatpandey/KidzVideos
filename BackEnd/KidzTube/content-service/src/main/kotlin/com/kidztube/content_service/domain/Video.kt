package com.kidztube.content_service.domain

import jakarta.persistence.*
import java.util.Date

@Entity
data class Video(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @Column(unique = true)
    val name: String,

    @Column
    val genere: String,

    @Column
    val isTrending: String,

    @Column
    val columnLastChanged: String,

    @Column
    val thumbnailName: String,

    @Column
    val ageGroup: String,

    @Column
    val description: String,

    @Column(name = "video_key")
    val videoKey: String,

    @Column(name = "thumbnail_key")
    var thumbnailKey: String? = null
)
