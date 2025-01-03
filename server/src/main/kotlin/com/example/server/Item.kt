package com.example.server

import com.example.server.entity.ItemTableEntity
import com.example.server.entity.MainTableEntity
import java.util.*

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
    val id: UUID,
    val name: String,
    val price: Int,
    val quantity: Int,
    val primaryCategory: PrimaryCategory,
    val secondaryCategory: SecondaryCategory,
)

fun Item.toItemTableEntity(): ItemTableEntity {
    return ItemTableEntity(
        id = this.id.toString(),
        name = this.name,
        price = this.price,
        quantity = this.quantity,
        primaryCategoryAndSecondaryCategory = "${this.primaryCategory}#${this.secondaryCategory}",
    )
}

fun Item.toMainTableEntity(): MainTableEntity {
    return MainTableEntity(
        pk = "Item#${this.secondaryCategory}",
        sk = this.id.toString(),
        name = this.name,
        price = this.price,
        quantity = this.quantity,
        primaryCategory = this.primaryCategory.toString(),
        secondaryCategory = this.secondaryCategory.toString(),
    )
}
