package com.z.h2jpa.service

import com.z.h2jpa.dao.ProviderRepository
import com.z.h2jpa.domain.Provider
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProviderService(private val providerRepository: ProviderRepository):BasicCrud<Provider,Int> {
    override fun findAll(pageable: Pageable): Page<Provider> {
        return providerRepository.findAll(pageable)
    }

    override fun findById(id: Int): Provider? {
        return providerRepository.findById(id).orElse(null)
    }

    override fun insert(entity: Provider): Provider {
        entity.id?.let { throw Exception("Entity must not have ID") }
        entity.address?.provider = entity
        return providerRepository.save(entity)
    }

    override fun update(entity: Provider): Provider {
        entity.id?.let {
            if(!providerRepository.existsById(it)) throw Exception("Entity with id: $it does not exists")
            return providerRepository.save(entity)
        }
        throw Exception("Entity must have ID")
    }

    override fun deleteById(id: Int)  = providerRepository.deleteById(id)
}