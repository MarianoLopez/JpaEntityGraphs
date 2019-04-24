package com.z.h2jpa.controller

import com.z.h2jpa.domain.Product
import com.z.h2jpa.domain.Ticket
import com.z.h2jpa.service.ProductService
import com.z.h2jpa.service.TicketService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductService): BasicController<Product, Int>(productService)

@RestController
@RequestMapping("/ticket")
class TicketController(private val ticketService: TicketService): BasicController<Ticket,Int>(ticketService){

    @ApiOperation("Should throw a DataIntegrityViolationException and rollback the transaction")
    @PostMapping("/transaction-test")
    fun transactionTest(@RequestBody ticket: Ticket) = ticketService.tryTransaction(ticket)
}