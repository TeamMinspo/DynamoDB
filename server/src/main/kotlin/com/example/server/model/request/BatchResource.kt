package com.example.server.model.request

import com.example.server.entity.TableEntity

data class BatchResource<PK, SK>(
    val tableEntity: Class<out TableEntity>,
    val primaryKeys: List<PrimaryKey<PK, SK>>,
)
