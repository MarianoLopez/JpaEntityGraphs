package com.z.h2jpa.utils

import com.z.h2jpa.dto.AttributeError
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError

fun BindingResult.toAttributeErrors() = this.fieldErrors.groupBy({it.field},{it.defaultMessage ?: ""}).map { AttributeError(field = it.key, errors = it.value) }