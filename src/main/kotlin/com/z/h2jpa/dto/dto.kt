package com.z.h2jpa.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ApiResponse(
        val title:String,
        val message:String,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        val date: LocalDateTime = LocalDateTime.now(),
        val payload: Any = Any())

data class AttributeError(val field:String, val errors:List<String>)