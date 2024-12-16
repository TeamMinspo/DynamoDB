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
class ItemController {
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
        return items.filter { it.secondaryCategory == category }
    }

    val items: List<Item> = listOf(
        Item(name = "松坂牛", price = 15000, quantity = 5, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.MEAT),
        Item(name = "飛騨牛", price = 13000, quantity = 10, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.MEAT),
        Item(name = "アジ", price = 200, quantity = 1000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.FISH),
        Item(name = "サーモン", price = 8000, quantity = 800, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.FISH),
        Item(name = "コーラ", price = 150, quantity = 5000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.DRINK),
        Item(name = "キリンレモン", price = 130, quantity = 6000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.DRINK),
        Item(name = "みそきん", price = 300, quantity = 100, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.NOODLE),
        Item(name = "どん兵衛", price = 150, quantity = 4000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.NOODLE),
        Item(name = "アサヒスーパードライ", price = 200, quantity = 4500, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.ALCOHOL),
        Item(name = "ほろよい", price = 100, quantity = 9000, primaryCategory = PrimaryCategory.FOOD, secondaryCategory = SecondaryCategory.ALCOHOL),

        Item(name = "DIG-Tシャツ", price = 3000, quantity = 100, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.TOPS),
        Item(name = "ロンT", price = 3500, quantity = 200, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.TOPS),
        Item(name = "コート", price = 10000, quantity = 150, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.OUTER),
        Item(name = "ダウンジャケット", price = 30000, quantity = 50, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.OUTER),
        Item(name = "ジーンズ", price = 5000, quantity = 500, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BOTTOMS),
        Item(name = "のび太の短パン", price = 500, quantity = 1400, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BOTTOMS),
        Item(name = "ニット帽", price = 2000, quantity = 1200, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.CAPS),
        Item(name = "大谷レプリカ", price = 4500, quantity = 500, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.CAPS),
        Item(name = "リング", price = 1000, quantity = 2000, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.ACCESSORIES),
        Item(name = "ネックレス", price = 3000, quantity = 2200, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.ACCESSORIES),
        Item(name = "リュック", price = 7000, quantity = 450, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BAGS),
        Item(name = "トートバッグ", price = 4000, quantity = 530, primaryCategory = PrimaryCategory.FASHION, secondaryCategory = SecondaryCategory.BAGS),

        Item(name = "Mac book pro", price = 500000, quantity = 20, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.PC),
        Item(name = "Mac book air", price = 150000, quantity = 240, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.PC),
        Item(name = "Pro Display XDR", price = 700000, quantity = 10, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MONITOR),
        Item(name = "Studio Display", price = 200000, quantity = 100, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MONITOR),
        Item(name = "Lofree flow", price = 30000, quantity = 500, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.KEYBOARD),
        Item(name = "HHKB", price = 30000, quantity = 600, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.KEYBOARD),
        Item(name = "Magic Mouse", price = 10000, quantity = 1300, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MOUSE),
        Item(name = "Magic Trackpad", price = 22000, quantity = 1200, primaryCategory = PrimaryCategory.ELECTRONICS, secondaryCategory = SecondaryCategory.MOUSE),

        Item(name = "FLEXSPOT", price = 50000, quantity = 2200, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.DESK),
        Item(name = "ニトリ机", price = 10000, quantity = 4200, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.DESK),
        Item(name = "COFO Chair Pro", price = 90000, quantity = 25, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.CHAIR),
        Item(name = "Ergohuman", price = 200000, quantity = 5, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.CHAIR),
        Item(name = "フリクション", price = 100, quantity = 30000, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.STATIONERY),
        Item(name = "ジェットストリーム", price = 100, quantity = 40000, primaryCategory = PrimaryCategory.OFFICE, secondaryCategory = SecondaryCategory.STATIONERY),
    )
}