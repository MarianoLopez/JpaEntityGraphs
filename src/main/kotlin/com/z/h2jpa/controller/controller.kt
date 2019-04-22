package com.z.h2jpa.controller

import com.z.h2jpa.domain.Product
import com.z.h2jpa.domain.Ticket
import com.z.h2jpa.service.ProductService
import com.z.h2jpa.service.TicketService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product")
class ProductController(private val productService: ProductService): BasicController<Product, Int>(productService)

@RestController
@RequestMapping("/ticket")
class TicketController(private val ticketService: TicketService): BasicController<Ticket,Int>(ticketService){
}