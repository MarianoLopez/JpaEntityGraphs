package com.z.h2jpa.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.z.h2jpa.utils.Auditor
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@NamedEntityGraphs(
    NamedEntityGraph(name = "Product.default",attributeNodes = [NamedAttributeNode("provider")])
)
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    @NotNull
    val name:String,
    val price:Double,
    @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
    @ManyToMany(mappedBy = "products")
    val tickets:MutableList<Ticket> = mutableListOf(),
    @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
    @ManyToOne(fetch = FetchType.LAZY)
    var provider: Provider
): Auditor()


@Entity
@NamedEntityGraphs(
    NamedEntityGraph(
      name = "Ticket.default",
      attributeNodes = [NamedAttributeNode("products",subgraph = "provider")],
      subgraphs = [
          NamedSubgraph(name = "provider",attributeNodes = [NamedAttributeNode("provider",subgraph = "address")]),
          NamedSubgraph(name = "address",attributeNodes = [NamedAttributeNode("address")])
      ])
)
data class Ticket(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ticket_detail", joinColumns = [JoinColumn(name = "id")], inverseJoinColumns = [JoinColumn(name = "productId")])
    val products:MutableList<Product> = mutableListOf()
): Auditor()

@Entity
@NamedEntityGraphs(NamedEntityGraph(name = "Provider.default", attributeNodes = [NamedAttributeNode("address")]))
data class Provider(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val name:String,
    @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
    @OneToMany(mappedBy = "provider",fetch = FetchType.LAZY)
    val products: MutableList<Product> = mutableListOf(),
    @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
    @OneToOne(fetch = FetchType.LAZY,cascade = [CascadeType.ALL],mappedBy = "provider", orphanRemoval = true)
    var address: Address? = null
)

@Entity
data class Address(
        @Id  @GenericGenerator(name = "generator", strategy = "foreign", parameters = [Parameter(name = "property", value = "provider")])//@GeneratedValue(strategy = GenerationType.AUTO)
        @GeneratedValue(generator = "generator") @Column(name = "provider_id")
        val id:Int? = null,
        val street:String,
        val city:String,
        @OneToOne(fetch = FetchType.LAZY, optional = false)//optional false enables FK
        @PrimaryKeyJoinColumn
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        val provider: Provider? = null
)