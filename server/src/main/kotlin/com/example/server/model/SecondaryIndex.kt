package com.example.server.model

interface SecondaryIndex<PK, SK> {
    val indexName: String
    val pk: PK
    val sk: SK?
}