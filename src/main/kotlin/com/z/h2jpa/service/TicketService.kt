package com.z.h2jpa.service

import com.z.h2jpa.dao.TicketRepository
import com.z.h2jpa.domain.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TicketService( val ticketRepository: TicketRepository):BasicCrud<Ticket,Int> {
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<Ticket> = ticketRepository.findAll(pageable)

    @Transactional(readOnly = true)
    override fun findById(id: Int): Ticket? = ticketRepository.findById(id).orElse(null)

    override fun insert(entity: Ticket): Ticket {
        entity.id?.let { throw Exception("Entity must not have ID") }
        return ticketRepository.save(entity)
    }

    fun tryTransaction(entity: Ticket):Ticket{
        entity.products[1].id = -9999
        return this.insert(entity)
    }

    override fun update(entity: Ticket): Ticket {
        entity.id?.let {
            if(!ticketRepository.existsById(it)) throw Exception("Entity with id: $it does not exists")
            return ticketRepository.save(entity)
        }
        throw Exception("Entity must have ID")
    }

    override fun deleteById(id: Int) = ticketRepository.deleteById(id)
}