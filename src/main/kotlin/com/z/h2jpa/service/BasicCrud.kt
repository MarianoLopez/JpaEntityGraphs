package com.z.h2jpa.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BasicCrud<T,ID> {
    fun findAll(pageable: Pageable):Page<T>
    fun findById(id:ID):T?
    @Throws(Exception::class)
    fun insert(entity:T):T
    @Throws(Exception::class)
    fun update(entity: T):T
    fun deleteById(id:ID)
}