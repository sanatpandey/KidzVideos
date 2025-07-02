package com.kidztube.userservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT) // exception for user
class UserAlreadyExistsException(message: String?) : RuntimeException(message)