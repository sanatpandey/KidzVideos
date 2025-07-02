package com.kidztube.content_service.constants

enum class platform(name: String) {
    NETFLIX("Netflix"),
    PRIME_VIDEO("Prime Video"),
    HOTSTAR("Hotstar");

    val videoName: String

    init {
        this.videoName = name
    }
}