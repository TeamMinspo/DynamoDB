package com.example.server.repository

import com.example.server.OrderMetadata
import com.example.server.ReceivedOrder
import com.example.server.entity.MainTableEntity
import com.example.server.entity.toCartItem
import com.example.server.entity.toOrderMetadata
import com.example.server.toMainTableEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import java.util.*

interface OrderRepository {
    fun findAllOrders(): List<ReceivedOrder>
    fun saveOrder(order: ReceivedOrder): ReceivedOrder
    fun findOrderById(id: UUID): ReceivedOrder?
}

@Repository
class DynamoDBOrderRepository(
    dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    @Value("\${dynamodb.table-name-suffix}")
    tableNameSuffix: String,
):
    OrderRepository,
    DynamoDBRepository<MainTableEntity, String, String>(dynamoDbEnhancedClient, tableNameSuffix)
{
    override fun findAllOrders(): List<ReceivedOrder> {
        return super.findAllByPKAndSKBeginsWith("Order#", "#Metadata")
            .map { tableEntityOfOrderMetadata ->
                val metadata = tableEntityOfOrderMetadata.toOrderMetadata()
                val cartItems = super.findAllByPKAndSKBeginsWith("Order#${metadata.orderId}", "Item#").map { it.toCartItem() }
                return@map ReceivedOrder(
                    id = metadata.orderId,
                    items = cartItems,
                    customerName = metadata.customerName,
                    status = metadata.status,
                )
            }
    }

    override fun saveOrder(order: ReceivedOrder): ReceivedOrder {
        val metadata = OrderMetadata(
            orderId = order.id,
            customerName = order.customerName,
            status = order.status,
        )
        val cartItems = order.items
        super.save(metadata.toMainTableEntity())
        super.saveInTransaction(cartItems.map { it.toMainTableEntity(order.id) })
        return order
    }

    override fun findOrderById(id: UUID): ReceivedOrder? {
        val metadata = findByPrimaryKeys("Order#${id}", "#Metadata")?.toOrderMetadata() ?: return null
        val cartItems = findAllByPKAndSKBeginsWith("Order#${id}", "Item#").map { it.toCartItem() }
        return ReceivedOrder(
            id = metadata.orderId,
            status = metadata.status,
            customerName = metadata.customerName,
            items = cartItems,
        )
    }
}