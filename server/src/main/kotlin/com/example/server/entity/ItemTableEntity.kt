package com.example.server.entity

import com.example.server.Item
import com.example.server.PrimaryCategory
import com.example.server.SecondaryCategory
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import java.util.*

@DynamoDbBean
data class ItemTableEntity(
    @get:DynamoDbPartitionKey
    var id: String = "",

    @get:DynamoDbSortKey
    var primaryCategoryAndSecondaryCategory: String ="",

    var name: String = "",
    var price: Int = 0,
    var quantity: Int = 0,
): TableEntity {
    override val tableName: String
        get() = "item_table"
}

fun ItemTableEntity.toItem(): Item {
    return Item(
        id = UUID.fromString(this.id),
        name = this.name,
        price = this.price,
        quantity = this.quantity,
        primaryCategory = PrimaryCategory.valueOf(this.primaryCategoryAndSecondaryCategory.split("#")[0]),
        secondaryCategory = SecondaryCategory.valueOf(this.primaryCategoryAndSecondaryCategory.split("#")[1]),
    )
}
