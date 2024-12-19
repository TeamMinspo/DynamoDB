package com.example.server.model.request

data class PrimaryKey<PK, SK>(
    val pk: PK,
    val sk: SK?
)