package com.kidztube.content_service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "A Video with this name already exists")
class VideoAlreadyExistsException: RuntimeException() {
}