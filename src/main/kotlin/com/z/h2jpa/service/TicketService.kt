package com.z.h2jpa.service

import com.z.h2jpa.dao.TicketRepository
import com.z.h2jpa.domain.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = false)
class TicketService( val ticketRepository: TicketRepository):BasicCrud<Ticket,Int> {

    override fun findAll(pageable: Pageable): Page<Ticket> = ticketRepository.findAll(pageable)

    override fun findById(id: Int): Ticket? = ticketRepository.findById(id).orElse(null)

    override fun insert(entity: Ticket): Ticket {
        entity.id?.let { throw Exception("Entity must not have ID") }
        return ticketRepository.save(entity)
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