package com.z.h2jpa.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.z.h2jpa.utils.Auditor
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.Size


@Entity
data class Product(
        @Id
        @Column(nullable = false)
        var id: Int,

        @Column(unique = true, nullable = false)
        val name:String,

        @get:Min(0)
        var stock: Int = 0,

        @get:Min(0)
        var price: Double = 0.0,
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        @OneToMany(mappedBy = "product", cascade = [CascadeType.PERSIST])
        var ticketDetails: MutableList<TicketDetail> = mutableListOf()
):Auditor()

@Entity
data class Ticket(
        @Id
        @GeneratedValue
        var id: Int? = 0,
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @OneToMany(mappedBy = "ticket", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
        val ticketDetails:MutableList<TicketDetail> = mutableListOf(),

        @get:Min(0)
        var total:Double = 0.0
):Auditor() {
    fun addDetail(vararg ticketDetail: TicketDetail) {
        ticketDetail.forEach {
            this.accumulateTotal(it)
            it.updateStock()
        }
        this.ticketDetails.addAll(ticketDetail)
    }

    private fun accumulateTotal(ticketDetail: TicketDetail) {
        this.total += ticketDetail.quantity * ticketDetail.product.price
    }
}

@Entity
@IdClass(TicketDetailId::class)
data class TicketDetail(
        @Id
        @JoinColumn(name = "ticket_id", referencedColumnName = "id")
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        var ticket: Ticket,


        @Id
        @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false)
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        var product: Product,

        @get:Min(1)
        var quantity: Int = 0
) {
    fun updateStock() {
        this.product.stock = this.product.stock - this.quantity
    }
}

class TicketDetailId(var ticket:Int = 0, var product:Int = 0) : Serializable {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(this === other) return true
        if(this.javaClass != other.javaClass) return false
        other as TicketDetailId
        return this.ticket == other.ticket && this.product == other.product
    }

    override fun hashCode(): Int {
        var result = 7
        result = 31 * result + ticket.hashCode()
        result = 31 * result + product.hashCode()
        return result
    }
}


/*
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
)*/

@Entity
data class Provider(
        @Id @GeneratedValue
        @ApiModelProperty(example = "1")
        val id: Int? = 0,

        @ApiModelProperty(example = "Mariano")
        @Column(nullable = false)
        @get:Size(min = 4, max = 100)
        val name: String,

        @ApiModelProperty(readOnly = true)//will be omitted at the swagger input
        @JsonBackReference //the associated property is part of two-way linkage between fields; and that its role is "child" (or "back") link
        @OneToMany(mappedBy = "provider", fetch = FetchType.LAZY, cascade = [CascadeType.MERGE])
        val products: MutableList<Product> = mutableListOf()
)

/*@Entity
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
) : Auditor()*/
