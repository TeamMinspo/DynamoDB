package com.example.server.repository

import com.example.server.Item
import com.example.server.SecondaryCategory
import com.example.server.entity.MainTableEntity
import com.example.server.entity.toItem
import com.example.server.toMainTableEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import java.util.*

interface ItemRepository {
    fun findAllBySecondary(category: SecondaryCategory): List<Item>
    fun findById(itemId: UUID): Item?
    fun saveItem(item: Item): Item
}

@Repository
class DynamoDBItemRepository(
    dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    @Value("\${dynamodb.table-name-suffix}")
    tableNameSuffix: String,
):
    ItemRepository,
    DynamoDBRepository<MainTableEntity, String, String>(dynamoDbEnhancedClient, tableNameSuffix)
{
    override fun findAllBySecondary(category: SecondaryCategory): List<Item> {
        return super.findAllByPK("Item#${category}").map { it.toItem() }
    }

    override fun findById(itemId: UUID): Item? {
        return super.findAllByPKAndSKBeginsWith("Item#", "").filter { it.sk == itemId.toString() }.map { it.toItem() }.firstOrNull()
    }

    override fun saveItem(item: Item): Item {
        super.save(item.toMainTableEntity())
        return item
    }
}
