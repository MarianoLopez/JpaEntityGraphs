package com.z.h2jpa.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.z.h2jpa.utils.Auditor
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@Entity
@NamedEntityGraphs(NamedEntityGraph(name = "Product.default", attributeNodes = [NamedAttributeNode("provider")]))
data class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO) //AUTO = IDENTITY || SEQUENCE. will be defined be the engine
        val id: Int? = null,
        @ApiModelProperty(example = "Apple")
        @Size(min = 4, max = 250)
        @Column(unique = true, nullable = false)
        val name: String,
        @ApiModelProperty(example = "35.0")
        @NotNull @Size(min = 0)
        val price: Double,
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        @ManyToMany(mappedBy = "products")//bidirectional relationship
        val tickets: MutableList<Ticket> = mutableListOf(),
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @ManyToOne(fetch = FetchType.LAZY)
        var provider: Provider? = null
) : Auditor()


@Entity
@NamedEntityGraphs(
        NamedEntityGraph(
                name = "Ticket.default",
                attributeNodes = [NamedAttributeNode("products", subgraph = "provider")],
                subgraphs = [
                    NamedSubgraph(name = "provider", attributeNodes = [NamedAttributeNode("provider", subgraph = "address")]),
                    NamedSubgraph(name = "address", attributeNodes = [NamedAttributeNode("address")])
                ])
)
data class Ticket(
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int? = null,
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "ticket_detail", joinColumns = [JoinColumn(name = "id")], inverseJoinColumns = [JoinColumn(name = "productId")])
        val products: MutableList<Product> = mutableListOf()
) : Auditor()

@Entity
@NamedEntityGraphs(NamedEntityGraph(name = "Provider.default", attributeNodes = [NamedAttributeNode("address")]))
data class Provider(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Int? = null,
        @ApiModelProperty(example = "Mariano")
        @Size(min = 4, max = 100)
        val name: String,
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY)
        val products: MutableList<Product> = mutableListOf(),
        @NotNull
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL],//the persistence will propagate (cascade) to the relating entities.
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
        @Size(min = 4, max = 100)
        val street: String,
        @ApiModelProperty(example = "Corrientes")
        @Size(min = 4, max = 100)
        val city: String,
        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @OneToOne(fetch = FetchType.LAZY, optional = false)//optional false enables FK
        @PrimaryKeyJoinColumn//This annotation specifies a primary key column that is used as a foreign key to join to another table.
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        val provider: Provider? = null
) : Auditor()