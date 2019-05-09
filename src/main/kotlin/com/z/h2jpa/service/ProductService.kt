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

    override fun insert(entity: Product): Product = productRepository.save(entity)

    override fun update(entity: Product): Product {
        if(!productRepository.existsById(entity.id)) throw Exception("Entity with id: ${entity.id} does not exists")
        return productRepository.save(entity)
    }

    override fun deleteById(id: Int) = productRepository.deleteById(id)

}