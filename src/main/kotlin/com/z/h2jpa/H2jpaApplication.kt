package com.z.h2jpa

import com.z.h2jpa.dao.ProductRepository
import com.z.h2jpa.dao.ProviderRepository
import com.z.h2jpa.dao.TicketRepository
import com.z.h2jpa.domain.Address
import com.z.h2jpa.domain.Product
import com.z.h2jpa.domain.Provider
import com.z.h2jpa.domain.Ticket
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class H2jpaApplication(private val productRepository: ProductRepository, private val ticketRepository: TicketRepository, private val providerRepository: ProviderRepository):ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        populateDB()
	}

    private fun populateDB(){
        var provider = Provider(name = "Mariano")
        provider.address =  Address(street = "123",city = "Corrientes",provider = provider)
        provider = providerRepository.save(provider)
        val products = productRepository.saveAll(listOf("p1","p2","p3").map { Product(name = it, provider = provider,price = (100..1000).random().toDouble())}).toList()
        ticketRepository.save(Ticket(products = products.slice(1..2).toMutableList()))
    }
}

fun main(args: Array<String>) {
	runApplication<H2jpaApplication>(*args)
}






