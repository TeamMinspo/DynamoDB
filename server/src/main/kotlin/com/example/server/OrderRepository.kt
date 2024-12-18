package com.example.server

import org.springframework.stereotype.Repository
import java.util.*

interface OrderRepository {
    fun findAll(): List<ReceivedOrder>
    fun save(order: ReceivedOrder): ReceivedOrder
    fun findById(id: UUID): ReceivedOrder?
}

@Repository
class InMemoryOrderRepository : OrderRepository {
    private var orders: MutableList<ReceivedOrder> = mutableListOf()

    override fun findAll(): List<ReceivedOrder> {
        return orders
    }

    override fun save(order: ReceivedOrder): ReceivedOrder {
        orders.removeIf { it.id == order.id }
        orders.add(order)
        return order
    }

    override fun findById(id: UUID): ReceivedOrder? {
        return orders.find { it.id == id }
    }
}