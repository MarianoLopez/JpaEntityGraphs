package com.z.h2jpa.controller

import com.z.h2jpa.dto.ApiResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler:ResponseEntityExceptionHandler(){
    @ExceptionHandler(Exception::class)
    fun handleAnyException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex,
                ApiResponse(title = ex::class.java.simpleName, message = ex.localizedMessage ?: ex.message ?: "error"),
                HttpHeaders(),
                HttpStatus.BAD_REQUEST,request)
    }
}