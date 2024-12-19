package com.example.server.model

import com.example.server.entity.TableEntity

data class BatchResponse(
    val items: List<TableEntity>
)
