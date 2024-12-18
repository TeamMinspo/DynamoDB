package com.example.server

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val itemRepository: ItemRepository,
    private val orderRepository: OrderRepository,
) {


    @GetMapping
    fun getAllOrders(): List<ReceivedOrder> {
        return orderRepository.findAll()
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
        orderRepository.save(receivedOrder)
        return receivedOrder.id
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: UUID): ReceivedOrder {
        return orderRepository.findById(orderId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
    }

    @PatchMapping("/{orderId}")
    fun updateOrderById(@PathVariable orderId: UUID, @RequestBody order: ReceivedOrder) {
        orderRepository.findById(orderId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
        orderRepository.save(order)
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