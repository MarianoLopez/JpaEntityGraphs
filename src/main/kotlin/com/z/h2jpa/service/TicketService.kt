package com.z.h2jpa.service

import com.z.h2jpa.dao.TicketRepository
import com.z.h2jpa.domain.Ticket
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TicketService(private val ticketRepository: TicketRepository, private val productService: ProductService):BasicCrud<Ticket,Int> {
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<Ticket> = ticketRepository.findAll(pageable)

    @Transactional(readOnly = true)
    override fun findById(id: Int): Ticket? = ticketRepository.findById(id).orElse(null)

    override fun insert(entity: Ticket): Ticket {
        entity.id?.let { throw Exception("Entity must not have ID") }
        this.setBiDirectionalTicketBindingAndUpdateProductStock(entity)
        return ticketRepository.save(entity)
    }

    fun tryTransaction(entity: Ticket):Ticket{
        entity.detail[0].product.id = -9999
        return ticketRepository.save(entity)
    }

    override fun update(entity: Ticket): Ticket {
        entity.id?.let {
            if(!ticketRepository.existsById(it)) throw Exception("Entity with id: $it does not exists")
            this.setBiDirectionalTicketBindingAndUpdateProductStock(entity)
            return ticketRepository.save(entity)
        }
        throw Exception("Entity must have ID")
    }

    override fun deleteById(id: Int) = ticketRepository.deleteById(id)

    private fun setBiDirectionalTicketBindingAndUpdateProductStock(entity: Ticket){
        entity.detail.forEach {
            it.ticket = entity //bi-directional binding needed for @Id
            productService.update(it.product.apply {
                stock -= it.quantity
            })
        }//update products stock
    }
}