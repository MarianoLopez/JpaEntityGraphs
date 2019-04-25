package com.z.h2jpa.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.h2jpa.dto.ApiResponse
import com.z.h2jpa.utils.toAttributeErrors
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler(private val mapper: ObjectMapper):ResponseEntityExceptionHandler(){
    @ExceptionHandler(Exception::class)
    fun handleAnyException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        return toResponse(ex,request)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return toResponse(ApiResponse(title = ex::class.java.simpleName,message = "", payload = mapper.createObjectNode().putPOJO("errors",ex.bindingResult.toAttributeErrors())),ex,request,headers,status)
    }

    private fun toResponse(ex:Exception, req:WebRequest, headers: HttpHeaders = HttpHeaders(), status: HttpStatus = HttpStatus.BAD_REQUEST): ResponseEntity<Any> {
        return this.toResponse(ApiResponse(title = ex::class.java.simpleName, message = ex.localizedMessage ?: ex.message ?: "error"),
                ex, req, headers, status)
    }
    private fun toResponse(apiResponse: ApiResponse,ex:Exception, req:WebRequest, headers: HttpHeaders = HttpHeaders(), status: HttpStatus = HttpStatus.BAD_REQUEST): ResponseEntity<Any> {
        return handleExceptionInternal(ex, apiResponse, headers, status, req)
    }
}