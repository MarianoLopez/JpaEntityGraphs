package com.z.h2jpa.controller

import com.z.h2jpa.service.BasicCrud
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*


abstract class BasicController<T,ID>(private val basicCrud: BasicCrud<T,ID>) {
    @GetMapping fun findAll(pageable: Pageable): Page<T> = basicCrud.findAll(pageable)
    @GetMapping("/{id}") fun findById(@PathVariable id:ID):T? = basicCrud.findById(id)
    @PostMapping fun insert(@RequestBody t:T):T = basicCrud.insert(t)
    @PutMapping fun update(@RequestBody t:T):T = basicCrud.update(t)
    @DeleteMapping("/{id}") fun deleteById(@PathVariable id:ID) = basicCrud.deleteById(id)
}