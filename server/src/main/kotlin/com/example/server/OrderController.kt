package com.example.server

import com.example.server.entity.MainTableEntity
import com.example.server.repository.ItemRepository
import com.example.server.repository.OrderRepository
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
        return orderRepository.findAllOrders()
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
                itemRepository.saveItem(newItem)
            }
        }

        val receivedOrder = ReceivedOrder(
            items = order.items,
            customerName = order.customerName,
            status = OrderStatus.RECEIVED
        )
        orderRepository.saveOrder(receivedOrder)
        return receivedOrder.id
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: UUID): ReceivedOrder {
        return orderRepository.findOrderById(orderId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
    }

    @PatchMapping("/{orderId}")
    fun updateOrderById(@PathVariable orderId: UUID, @RequestBody order: ReceivedOrder) {
        orderRepository.findOrderById(orderId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")
        orderRepository.saveOrder(order)
    }
}

data class CartItem(
    val item: Item,
    val count: Int
)

fun CartItem.toMainTableEntity(orderId: UUID): MainTableEntity {
    return MainTableEntity(
        pk = "Order#${orderId}",
        sk = "Item#${this.item.id}",
        count = this.count,
        name = this.item.name,
        price = this.item.price,
        quantity = this.item.quantity,
        primaryCategory = this.item.primaryCategory.toString(),
        secondaryCategory = this.item.secondaryCategory.toString(),
    )
}

data class OrderMetadata(
    var orderId: UUID,
    var customerName: String,
    var status: OrderStatus,
)

fun OrderMetadata.toMainTableEntity(): MainTableEntity {
    return MainTableEntity(
        pk = "Order#${this.orderId}",
        sk = "#Metadata",
        orderStatus = this.status.toString(),
        customerName = this.customerName,
    )
}

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