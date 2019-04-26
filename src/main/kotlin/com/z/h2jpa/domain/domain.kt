package com.z.h2jpa.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.z.h2jpa.utils.Auditor
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Entity
@NamedEntityGraphs(
  NamedEntityGraph(
    name = "Product.default", attributeNodes = [NamedAttributeNode("provider",subgraph = "address")],
    subgraphs = [NamedSubgraph(name = "address",attributeNodes = [NamedAttributeNode(value = "address")])]))
data class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO) //AUTO = IDENTITY || SEQUENCE. will be defined be the engine
        var id: Int? = null,
        @ApiModelProperty(example = "Apple")
        @get:Size(min = 4, max = 250)
        @Column(unique = true, nullable = false)
        val name: String,
        @ApiModelProperty(example = "35.0")
        @get:Min(0)
        var price: Double,
        @ApiModelProperty(example = "12")
        @get:Min(0)
        var stock: Int,
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        @OneToMany(mappedBy = "product")//bidirectional relationship
        val ticketDetails: MutableList<TicketDetail> = mutableListOf(),
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @ManyToOne(fetch = FetchType.LAZY)
        var provider: Provider
) : Auditor()

@Entity
data class TicketDetail(
        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn //indicates that a given column in the owner entity refers to a primary key in the reference entity
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        var ticket: Ticket? = null,
        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        val product: Product,
        @get:Min(0)
        val quantity: Int,
        @get:Min(0)
        val total: Double
):Serializable //Entity with composite ID must implements Serializable

@Entity
@NamedEntityGraphs(
   NamedEntityGraph(
     name = "Ticket.default",
     attributeNodes = [NamedAttributeNode("detail", subgraph = "detail")],
     subgraphs = [
       NamedSubgraph(name = "detail",attributeNodes = [NamedAttributeNode("ticket"),NamedAttributeNode("product", subgraph = "provider")]),
       NamedSubgraph(name = "provider", attributeNodes = [NamedAttributeNode("provider", subgraph = "address")]),
       NamedSubgraph(name = "address", attributeNodes = [NamedAttributeNode("address")])
     ]
   )
)
data class Ticket(
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int? = null,
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket", cascade = [CascadeType.ALL])
        val detail: MutableList<TicketDetail> = mutableListOf(),
        @get:Min(0)
        var total: Double
) : Auditor()

@Entity
@NamedEntityGraphs(NamedEntityGraph(name = "Provider.default", attributeNodes = [NamedAttributeNode("address")]))
data class Provider(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        @ApiModelProperty(example = "1")
        val id: Int? = null,
        @ApiModelProperty(example = "Mariano")
        @get:Size(min = 4, max = 100)
        val name: String,
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        val products: MutableList<Product> = mutableListOf(),
        @NotNull
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @OneToOne(fetch = FetchType.LAZY,//the persistence will propagate (cascade) to the relating entities.
                mappedBy = "provider", orphanRemoval = true
        ) var address: Address? = null //allow null because needs to instance Address first which needs a provider
)

@Entity
data class Address(
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @Id @Column(name = "provider_id")
        @GenericGenerator(name = "generator", strategy = "foreign", parameters = [Parameter(name = "property", value = "provider")])//same id as provider.id
        @GeneratedValue(generator = "generator")
        val id: Int? = null,
        @ApiModelProperty(example = "Example 123")
        @get:Size(min = 4, max = 100)
        val street: String,
        @ApiModelProperty(example = "Corrientes")
        @get:Size(min = 4, max = 100)
        val city: String,
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @OneToOne(fetch = FetchType.LAZY, optional = false)//optional false enables FK
        @PrimaryKeyJoinColumn//This annotation specifies a primary key column that is used as a foreign key to join to another table.
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        var provider: Provider? = null
) : Auditor()