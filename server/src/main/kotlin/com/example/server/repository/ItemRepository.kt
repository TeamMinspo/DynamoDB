package com.example.server.repository

import com.example.server.Item
import com.example.server.entity.ItemTableEntity
import com.example.server.entity.toItem
import com.example.server.itemSeeds
import com.example.server.toItemTableEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import java.util.*

interface ItemRepository {
    fun findAllItems(): List<Item>
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
    DynamoDBRepository<ItemTableEntity, String, String>(dynamoDbEnhancedClient, tableNameSuffix)
{
    override fun findAllItems(): List<Item> {
        return super.findAll().map { it.toItem() }
    }

    override fun findById(itemId: UUID): Item? {
        return super.findAllByPK(itemId.toString()).firstOrNull()?.toItem()
    }

    override fun saveItem(item: Item): Item {
        super.save(item.toItemTableEntity())
        return item
    }
}

class InMemoryItemRepository: ItemRepository {
    override fun findAllItems(): List<Item> {
        return items
    }

    override fun findById(itemId: UUID): Item? {
        return items.find { it.id == itemId }
    }

    override fun saveItem(item: Item): Item {
        items.removeIf { it.id == item.id }
        items.add(item)
        return item
    }

    private var items: MutableList<Item> = itemSeeds.toMutableList()
}

