package com.z.h2jpa.dao

import com.z.h2jpa.domain.Product
import com.z.h2jpa.domain.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*


interface ProductRepository: PagingAndSortingRepository<Product, Int>{
    @EntityGraph(value = "Product.default")
    override fun findAll(p0: Pageable): Page<Product>

    @EntityGraph(value = "Product.default")
    override fun findById(id:Int):Optional<Product>
}

interface TicketRepository: PagingAndSortingRepository<Ticket, Int>{
    @EntityGraph(value = "Ticket.default")
    override fun findAll(pageable: Pageable): Page<Ticket>

    @EntityGraph(value = "Ticket.default")
    override fun findById(id:Int):Optional<Ticket>
}

//interface ProviderRepository: PagingAndSortingRepository<Provider,Int>