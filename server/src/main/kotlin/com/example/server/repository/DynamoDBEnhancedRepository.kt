package com.example.server.repository

import com.example.server.entity.TableEntity
import com.example.server.extension.setPrimaryKeys
import com.example.server.model.BatchResponse
import com.example.server.model.request.BatchResource
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetResultPageIterable
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest


interface NoSQLEnhancedRepository {
    fun saveInTransaction(items: List<TableEntity>)
    fun batchGetItems(resources: List<BatchResource<*, *>>): List<BatchResponse>
}

abstract class DynamoDBEnhancedRepository(
    private val dynamoDbEnhancedClient: DynamoDbEnhancedClient,
    private val tableNameSuffix: String
): NoSQLEnhancedRepository {
    override fun saveInTransaction(items: List<TableEntity>) {
        val requestBuilder = TransactWriteItemsEnhancedRequest.builder()
        items.forEach { item ->
            val dynamoDBTable = dynamoDbEnhancedClient.table(
                "${item.tableName}_$tableNameSuffix",
                TableSchema.fromBean(item.javaClass)
            )
            requestBuilder.addPutItem(dynamoDBTable, item)
        }
        requestBuilder.build() //Fixme: .build()を2回実行しているが、、、？

        dynamoDbEnhancedClient.transactWriteItems(requestBuilder.build())
    }

    override fun batchGetItems(resources: List<BatchResource<*, *>>): List<BatchResponse> {
        val readBatches = createReadBatches(resources)
        val batchGetResultPageIterable = executeBatchGet(readBatches)

        return createBatchResponses(resources, batchGetResultPageIterable)
    }

    private fun createReadBatches(resources: List<BatchResource<*, *>>): List<ReadBatch> {
        return resources.map { resource ->
            val dynamoDBTable = createDynamoDbTable(resource)
            val readBatchBuilder = ReadBatch
                .builder(resource.tableEntity)
                .mappedTableResource(dynamoDBTable)

            resource.primaryKeys.forEach { primaryKey ->
                readBatchBuilder.addGetItem(
                    Key.builder()
                        .setPrimaryKeys(primaryKey.pk, primaryKey.sk)
                        .build()
                )
            }

            readBatchBuilder.build()
        }
    }

    private fun executeBatchGet(readBatches: List<ReadBatch>): BatchGetResultPageIterable {
        val batchGetItemEnhancedRequest = BatchGetItemEnhancedRequest.builder()
            .readBatches(readBatches)
            .build()

        return dynamoDbEnhancedClient
            .batchGetItem(batchGetItemEnhancedRequest)
    }

    private fun createBatchResponses(
        resources: List<BatchResource<*, *>>,
        batchGetResultPageIterable: BatchGetResultPageIterable
    ): List<BatchResponse> {
        return resources.map { resource ->
            val dynamoDBTable = createDynamoDbTable(resource)

            val entities = batchGetResultPageIterable
                .resultsForTable(dynamoDBTable)
                .map { it } // Fixme: これ必要なのか？

            BatchResponse(items = entities)
        }
    }

    private fun createDynamoDbTable(resource: BatchResource<*, *>): DynamoDbTable<TableEntity> {
        val instance = resource.tableEntity.getConstructor().newInstance()
        return dynamoDbEnhancedClient.table(
            "${instance.tableName}_$tableNameSuffix",
            @Suppress("UNCHECKED_CAST")
            TableSchema.fromBean(resource.tableEntity as Class<TableEntity>)
        )
    }
}