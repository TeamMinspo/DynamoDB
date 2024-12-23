package com.example.server.entity

import com.example.server.*
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import java.util.*

@DynamoDbBean
data class MainTableEntity(
    @get:DynamoDbPartitionKey
    var pk: String = "",

    @get:DynamoDbSortKey
    var sk: String ="",

    var name: String? = null,
    var price: Int? = null,
    var quantity: Int? = null,
    var primaryCategory: String? = null,
    var secondaryCategory: String? = null,

    var orderStatus: String? = null,
    var customerName: String? = null,
    var count: Int? = null,
): TableEntity {
    override val tableName: String
        get() = "main_table"
}

fun MainTableEntity.toItem(): Item {
    return Item(
        id = UUID.fromString(this.sk),
        name = this.name ?: "",
        price = this.price ?: 0,
        quantity = this.quantity ?: 0,
        primaryCategory = PrimaryCategory.valueOf(this.primaryCategory ?: "FOOD"),
        secondaryCategory = SecondaryCategory.valueOf(this.secondaryCategory ?: "MEAT"),
    )
}

fun MainTableEntity.toOrderMetadata(): OrderMetadata {
    return OrderMetadata(
        orderId = UUID.fromString(this.pk.split("#")[1]),
        customerName = this.customerName ?: "",
        status = OrderStatus.valueOf(this.orderStatus ?: "RECEIVED"),
    )
}

fun MainTableEntity.toCartItem(): CartItem {
    return CartItem(
        item = Item(
            id = UUID.fromString(this.sk.split("#")[1]),
            name = this.name ?: "",
            price = this.price ?: 0,
            quantity = this.quantity ?: 0,
            primaryCategory = PrimaryCategory.valueOf(this.primaryCategory ?: "FOOD"),
            secondaryCategory = SecondaryCategory.valueOf(this.secondaryCategory ?: "MEAT"),
        ),
        count = this.count ?: 0
    )
}
