package com.ege.teams.common

import com.ege.teams.common.Constants.GENERIC_ERROR_MESSAGE
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
open class CustomExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(NoSuchElementException::class)
    protected fun handleNoSuchArgument(ex: RuntimeException): ResponseEntity<String> {
        val message = ex.message ?: GENERIC_ERROR_MESSAGE
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message)
    }

    @ExceptionHandler(IllegalStateException::class)
    protected fun handleIllegalState(ex: RuntimeException): ResponseEntity<String> {
        val message = ex.message ?: GENERIC_ERROR_MESSAGE
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleServerError(ex: Exception): ResponseEntity<String> {
        val message = ex.message ?: GENERIC_ERROR_MESSAGE
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message)
    }
}