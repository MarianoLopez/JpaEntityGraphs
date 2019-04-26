package com.z.h2jpa.service

import com.z.h2jpa.dao.ProductRepository
import com.z.h2jpa.domain.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository): BasicCrud<Product,Int>{
    override fun findAll(pageable: Pageable): Page<Product> = productRepository.findAll(pageable)

    override fun findById(id: Int): Product? = productRepository.findById(id).orElse(null)

    override fun insert(entity: Product): Product {
        entity.id?.let { throw Exception("Entity must not have ID") }
        productRepository.save(entity)
        return productRepository.findById(entity.id!!).orElse(null) //if you want to include address info
    }

    override fun update(entity: Product): Product {
        entity.id?.let {
            if(!productRepository.existsById(it)) throw Exception("Entity with id: $it does not exists")
            return productRepository.save(entity)
        }
        throw Exception("Entity must have ID")
    }

    override fun deleteById(id: Int) = productRepository.deleteById(id)

}