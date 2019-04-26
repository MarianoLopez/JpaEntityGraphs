package com.z.h2jpa

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun ResultActions.toJson(objectMapper: ObjectMapper = jacksonObjectMapper()) = objectMapper.readValue<JsonNode>(this.andReturn().response.contentAsByteArray)

fun MockHttpServletRequestBuilder.body(body:Any, objectMapper: ObjectMapper = jacksonObjectMapper(), mediaType: MediaType = MediaType.APPLICATION_JSON_UTF8): MockHttpServletRequestBuilder {
    return this.apply {
        content(objectMapper.writeValueAsString(body))
        contentType(mediaType)
        accept(mediaType)
    }
}