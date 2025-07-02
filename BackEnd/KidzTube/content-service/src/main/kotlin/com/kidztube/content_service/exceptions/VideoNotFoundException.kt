package com.kidztube.content_service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A video with this name not found")
class VideoNotFoundException: RuntimeException() {
}