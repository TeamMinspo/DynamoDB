package com.example.server

import java.util.UUID

enum class PrimaryCategory {
    FOOD,
    FASHION,
    ELECTRONICS,
    OFFICE,
}

enum class SecondaryCategory {
    MEAT,
    FISH,
    DRINK,
    NOODLE,
    SNACK,
    ALCOHOL,
    TOPS,
    OUTER,
    BOTTOMS,
    CAPS,
    ACCESSORIES,
    BAGS,
    PC,
    MONITOR,
    KEYBOARD,
    MOUSE,
    DESK,
    CHAIR,
    STATIONERY,
}

data class Item(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val price: Int,
    val quantity: Int,
    val primaryCategory: PrimaryCategory,
    val secondaryCategory: SecondaryCategory,
)
