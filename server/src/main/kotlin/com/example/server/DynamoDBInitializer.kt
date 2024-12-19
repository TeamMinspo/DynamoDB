package com.example.server

import com.example.server.entity.ItemTableEntity
import com.example.server.repository.ItemRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException
import java.util.*

@Component
class DynamoDBInitializer(
    private val standardClient: DynamoDbClient,
    dynamoDBEnhancedClient: DynamoDbEnhancedClient,
    @Value("\${dynamodb.table-name-suffix}")
    private val tableNameSuffix: String,
    private val itemRepository: ItemRepository,
) {
    private val itemTable: DynamoDbTable<ItemTableEntity> = dynamoDBEnhancedClient
        .table(
            "item_table_${tableNameSuffix}",
            TableSchema.fromBean(ItemTableEntity::class.java)
        )
    @PostConstruct
    fun initializeDynamoDB() {
        createInformationTable()
    }

    private fun createInformationTable() {
        try {
            itemTable.createTable { builder ->
                builder.provisionedThroughput { throughput ->
                    throughput
                        .readCapacityUnits(10L)
                        .writeCapacityUnits(10L)
                        .build()
                }
            }
            waitForTableBecomeActive(itemTable)
            itemSeeds.forEach { item ->
                itemRepository.saveItem(item)
            }
        } catch (error: ResourceInUseException) {
            println("Main Table already exists...skip creating tables.")
        } catch (error: Exception) {
            println("Error creating item table")
            println("Error: ${error.message}")
        }
    }

    private fun <Table>waitForTableBecomeActive(dynamoDbTable: DynamoDbTable<Table>) {
        val waiter = standardClient.waiter()
        val request = DescribeTableRequest.builder()
            .tableName(dynamoDbTable.tableName())
            .build()

        try {
            waiter.waitUntilTableExists(request)
        } catch (error: Exception) {
            throw RuntimeException("Table did not become active within the specified time", error)
        }
    }
}

val itemSeeds: List<Item> = listOf(
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000036"), name = "神戸牛", price = 15000, quantity = 5, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.MEAT),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000035"), name = "飛騨牛", price = 13000, quantity = 10, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.MEAT),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000034"), name = "アジ", price = 200, quantity = 1000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.FISH),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000033"), name = "サーモン", price = 8000, quantity = 800, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.FISH),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000032"), name = "コーラ", price = 150, quantity = 5000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.DRINK),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000031"), name = "キリンレモン", price = 130, quantity = 6000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.DRINK),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000030"), name = "みそきん", price = 300, quantity = 100, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.NOODLE),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000029"), name = "どん兵衛", price = 150, quantity = 4000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.NOODLE),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000028"), name = "アサヒスーパードライ", price = 200, quantity = 4500, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.ALCOHOL),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000027"), name = "ほろよい", price = 100, quantity = 9000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.ALCOHOL),

    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000026"), name = "DIG-Tシャツ", price = 3000, quantity = 100, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.TOPS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000025"), name = "ロンT", price = 3500, quantity = 200, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.TOPS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000024"), name = "コート", price = 10000, quantity = 150, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.OUTER),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000023"), name = "ダウンジャケット", price = 30000, quantity = 50, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.OUTER),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000022"), name = "ジーンズ", price = 5000, quantity = 500, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BOTTOMS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000021"), name = "のび太の短パン", price = 500, quantity = 1400, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BOTTOMS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000020"), name = "ニット帽", price = 2000, quantity = 1200, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.CAPS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000019"), name = "大谷レプリカ", price = 4500, quantity = 500, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.CAPS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000018"), name = "リング", price = 1000, quantity = 2000, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.ACCESSORIES),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000017"), name = "ネックレス", price = 3000, quantity = 2200, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.ACCESSORIES),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000016"), name = "リュック", price = 7000, quantity = 450, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BAGS),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000015"), name = "トートバッグ", price = 4000, quantity = 530, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BAGS),

    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000014"), name = "Mac book pro", price = 500000, quantity = 20, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.PC),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000013"), name = "Mac book air", price = 150000, quantity = 240, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.PC),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000012"), name = "Pro Display XDR", price = 700000, quantity = 10, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MONITOR),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000011"), name = "Studio Display", price = 200000, quantity = 100, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MONITOR),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000010"), name = "Lofree flow", price = 30000, quantity = 500, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.KEYBOARD),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000009"), name = "HHKB", price = 30000, quantity = 600, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.KEYBOARD),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000008"), name = "Magic Mouse", price = 10000, quantity = 1300, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MOUSE),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000007"), name = "Magic Trackpad", price = 22000, quantity = 1200, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MOUSE),

    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000006"), name = "FLEXSPOT", price = 50000, quantity = 2200, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.DESK),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000005"), name = "ニトリ机", price = 10000, quantity = 4200, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.DESK),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000004"), name = "COFO Chair Pro", price = 90000, quantity = 25, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.CHAIR),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000003"), name = "Ergohuman", price = 200000, quantity = 5, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.CHAIR),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000002"), name = "フリクション", price = 100, quantity = 30000, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.STATIONERY),
    Item(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "ジェットストリーム", price = 100, quantity = 40000, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.STATIONERY),
)