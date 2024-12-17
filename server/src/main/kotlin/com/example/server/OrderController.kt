package com.example.server

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/api/orders")
class OrderController(private val itemRepository: ItemRepository) {
    private var orders: MutableList<ReceivedOrder> = mutableListOf()

    @GetMapping
    fun getAllOrders(): List<ReceivedOrder> {
        return orders
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody order: Order): UUID {
        order.items.forEach { cartItem ->
            val itemId = cartItem.item.id
            val item = itemRepository.findById(itemId) ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Item not found"
            )
            if (cartItem.count > item.quantity) {
                throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Item count can't be greater than quantity")
            } else {
                val newItem = item.copy(quantity = item.quantity - cartItem.count)
                itemRepository.save(newItem)
            }
        }

        val receivedOrder = ReceivedOrder(
            items = order.items,
            customerName = order.customerName,
            status = OrderStatus.RECEIVED
        )
        orders.add(receivedOrder)
        return receivedOrder.id
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: UUID): ReceivedOrder {
        return orders.find { it.id == orderId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
    }

    @PatchMapping("/{orderId}")
    fun updateOrderById(@PathVariable orderId: UUID, @RequestBody order: ReceivedOrder) {
        val orderBefore = orders.find { it.id == orderId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
        orders.remove(orderBefore)
        orders.add(order)
    }
}

data class CartItem(
    val item: Item,
    val count: Int
)

data class Order(
    val items: List<CartItem>,
    val customerName: String
)

data class ReceivedOrder(
    val id: UUID = UUID.randomUUID(),
    val items: List<CartItem>,
    val customerName: String,
    val status: OrderStatus,
)

enum class OrderStatus {
    RECEIVED, SHIPPED, DELIVERED
}