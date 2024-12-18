package com.example.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Category (
    val name: PrimaryCategory,
    val subCategories: List<SecondaryCategory>
)

@RestController
@RequestMapping("/api/items")
class ItemController(private val itemRepository: ItemRepository) {
    @GetMapping("/categories")
    fun getAllCategories(): List<Category> {
        return listOf(
            Category(
                PrimaryCategory.FOOD,
                listOf(
                    SecondaryCategory.MEAT,
                    SecondaryCategory.FISH,
                    SecondaryCategory.DRINK,
                    SecondaryCategory.NOODLE,
                    SecondaryCategory.SNACK,
                    SecondaryCategory.ALCOHOL,
                )
            ),
            Category(
                PrimaryCategory.FASHION,
                listOf(
                    SecondaryCategory.TOPS,
                    SecondaryCategory.OUTER,
                    SecondaryCategory.BOTTOMS,
                    SecondaryCategory.CAPS,
                    SecondaryCategory.ACCESSORIES,
                    SecondaryCategory.BAGS,
                )
            ),
            Category(
                PrimaryCategory.ELECTRONICS,
                listOf(
                    SecondaryCategory.PC,
                    SecondaryCategory.MONITOR,
                    SecondaryCategory.KEYBOARD,
                    SecondaryCategory.MOUSE,
                )
            ),
            Category(
                PrimaryCategory.OFFICE,
                listOf(
                    SecondaryCategory.DESK,
                    SecondaryCategory.CHAIR,
                    SecondaryCategory.STATIONERY,
                )
            ),
        )
    }

    @GetMapping("/{category}")
    fun getItemsByCategory(@PathVariable category: SecondaryCategory): List<Item> {
        return itemRepository.findAll().filter { it.secondaryCategory == category }
    }
}