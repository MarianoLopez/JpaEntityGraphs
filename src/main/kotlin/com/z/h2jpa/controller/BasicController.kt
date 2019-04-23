package com.z.h2jpa.controller

import com.z.h2jpa.service.BasicCrud
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*


abstract class BasicController<T,ID>(private val basicCrud: BasicCrud<T,ID>) {
    @ApiOperation("Find all entities with page request")
    @GetMapping fun findAll(pageable: Pageable): Page<T> = basicCrud.findAll(pageable)

    @ApiOperation("Find entity by ID")
    @GetMapping("/{id}") fun findById(@PathVariable id:ID):T? = basicCrud.findById(id)

    @ApiOperation("Insert entity. Throws exception if ID is not null")
    @Throws(Exception::class)
    @PostMapping fun insert(@RequestBody body:T):T = basicCrud.insert(body)

    @ApiOperation("Update entity. Throws exception if ID does not exists")
    @Throws(Exception::class)
    @PutMapping fun update(@RequestBody body:T):T = basicCrud.update(body)

    @ApiOperation("Delete entity by ID")
    @DeleteMapping("/{id}") fun deleteById(@PathVariable id:ID) = basicCrud.deleteById(id)
}