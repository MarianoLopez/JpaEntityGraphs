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
@NamedEntityGraphs(NamedEntityGraph(name = "Product.default", attributeNodes = [NamedAttributeNode("provider")]))
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
        var ticketDetails: MutableList<TicketDetail> = mutableListOf(),

        @JsonManagedReference //the annotated property is part of two-way linkage between fields; and that its role is "parent" (or "forward") link
        @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        var provider: Provider
):Auditor()

@Entity
@NamedEntityGraphs(
    NamedEntityGraph(
        name = "Ticket.default",
        attributeNodes = [NamedAttributeNode("ticketDetails", subgraph = "ticketDetails")],
        subgraphs = [
            NamedSubgraph(name = "ticketDetails",attributeNodes = [NamedAttributeNode("ticket"),NamedAttributeNode("product", subgraph = "provider")]),
            NamedSubgraph(name = "provider", attributeNodes = [NamedAttributeNode("provider")])
        ]
    )
)
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
    init {
        initDetails()
    }
    private fun initDetails() {
        this.ticketDetails.forEach {
            it.ticket = this //assign current ticket to each detail
            this.accumulateTotal(it) //accumulate the ticket total
            it.updateStock()
        }
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
        var ticket: Ticket? = null,


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
):Auditor()