package com.z.h2jpa

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.h2jpa.dao.ProductRepository
import com.z.h2jpa.dao.TicketRepository
import com.z.h2jpa.domain.Ticket
import com.z.h2jpa.domain.TicketDetail
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.Transactional

@SpringBootApplication
class H2jpaApplication(val mapper: ObjectMapper, val productRepository: ProductRepository, val ticketRepository: TicketRepository):ApplicationRunner {
	override fun run(args: ApplicationArguments?) {
		test()
	}

	@Transactional
	fun test() {
		val products = productRepository.findAllById(listOf(111,555)).toList()
		val ticket = Ticket(ticketDetails = mutableListOf(
			TicketDetail(product = products[0], quantity = 1),
			TicketDetail(product = products[1], quantity = 3)
		))

		ticketRepository.save(ticket)

		println(mapper.writeValueAsString(ticket))
	}
}

fun main(args: Array<String>) {
	runApplication<H2jpaApplication>(*args)
}






