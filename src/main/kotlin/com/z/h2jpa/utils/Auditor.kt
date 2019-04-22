package com.z.h2jpa.utils

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass //Designates a class whose mapping information is applied to the entities that inherit from it. A mapped superclass has no separate table defined for it.
@EntityListeners(AuditingEntityListener::class)
abstract class Auditor(
        @CreatedDate
        @Column(nullable = false, updatable = false)
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        protected var createdDate: LocalDateTime? = null,
        @LastModifiedDate
        @Column(nullable = false)
        @get:JsonProperty(access = JsonProperty.Access.READ_ONLY)
        protected var lastModifiedDate: LocalDateTime? = null
)