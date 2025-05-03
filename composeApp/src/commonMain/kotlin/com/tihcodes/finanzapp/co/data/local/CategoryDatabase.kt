package com.tihcodes.finanzapp.co.data.local

import androidx.compose.ui.graphics.Color
import com.finanzapp.Database
import com.tihcodes.finanzapp.co.data.CategoryItem
import com.tihcodes.finanzapp.co.utils.getSampleCategories
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_baby
import finanzapp_co.composeapp.generated.resources.ic_entertainmentame
import finanzapp_co.composeapp.generated.resources.ic_food
import finanzapp_co.composeapp.generated.resources.ic_gifts
import finanzapp_co.composeapp.generated.resources.ic_groceries
import finanzapp_co.composeapp.generated.resources.ic_home_expenses
import finanzapp_co.composeapp.generated.resources.ic_medicine
import finanzapp_co.composeapp.generated.resources.ic_moneysim
import finanzapp_co.composeapp.generated.resources.ic_savings
import finanzapp_co.composeapp.generated.resources.ic_savings_pig
import finanzapp_co.composeapp.generated.resources.ic_transport
import finanzapp_co.composeapp.generated.resources.ic_travel
import finanzapp_co.composeapp.generated.resources.ic_work
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.DrawableResource
import kotlin.random.Random

class CategoryDatabase(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = Database(
        driver = databaseDriverFactory.createDriver()
    )
    private val categoryQueries = database.categoryQueries

    suspend fun getAllCategories(): List<CategoryItem> = withContext(Dispatchers.Default) {
        println("INFO: Reading all categories from database")
        val categories = categoryQueries.getAllCategories().executeAsList()

        if (categories.isEmpty()) {
            // Return default categories if none exist in the database
            return@withContext getSampleCategories()
        }

        return@withContext categories.map { category ->
            CategoryItem(
                name = category.name,
                icon = parseIconFromString(category.icon),
                backgroundColor = parseColorFromString(category.color),
                nameReference = category.nameReference,
                userId = category.userId
            )
        }
    }

    suspend fun getCategoryById(id: String): CategoryItem? = withContext(Dispatchers.Default) {
        println("INFO: Reading category with id $id from database")
        val category = categoryQueries.getCategoryById(id).executeAsOneOrNull() ?: return@withContext null

        return@withContext CategoryItem(
            name = category.name,
            icon = parseIconFromString(category.icon),
            backgroundColor = parseColorFromString(category.color),
            nameReference = category.nameReference,
            userId = category.userId
        )
    }

    suspend fun insertCategory(category: CategoryItem) = withContext(Dispatchers.Default) {
        println("INFO: Inserting category into database")
        categoryQueries.insertCategory(
            id = generateId(),
            name = category.name,
            icon = getIconIdentifier(category.icon),
            color = getColorIdentifier(category.backgroundColor),
            nameReference = category.nameReference,
            userId = category.userId
        )
    }

    suspend fun updateCategory(id: String, category: CategoryItem) = withContext(Dispatchers.Default) {
        println("INFO: Updating category in database")
        categoryQueries.updateCategory(
            id = id,
            name = category.name,
            icon = getIconIdentifier(category.icon),
            color = getColorIdentifier(category.backgroundColor),
            nameReference = category.nameReference,
            userId = category.userId
        )
    }

    suspend fun deleteCategory(id: String) = withContext(Dispatchers.Default) {
        println("INFO: Deleting category from database")
        categoryQueries.deleteCategory(id)
    }

    private fun generateId(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    fun getCategoryByNameAndUserId(name: String, userId: String): CategoryItem? {
        return categoryQueries.getCategoryByNameAndUserId(name, userId).executeAsOneOrNull()?.let { category ->
            println("INFO: Reading category with name $name and userId $userId from database")
            CategoryItem(
                name = category.name,
                icon = parseIconFromString(category.icon),
                backgroundColor = parseColorFromString(category.color),
                nameReference = category.nameReference,
                userId = category.userId
            )
        }
    }

    private fun parseIconFromString(iconString: String): DrawableResource {
        return when (iconString) {
            "ic_gifts" -> Res.drawable.ic_gifts
            "ic_food" -> Res.drawable.ic_food
            "ic_transport" -> Res.drawable.ic_transport
            "ic_baby" -> Res.drawable.ic_baby
            "ic_work" -> Res.drawable.ic_work
            "ic_travel" -> Res.drawable.ic_travel
            "ic_entertainmentame" -> Res.drawable.ic_entertainmentame
            "ic_moneysim" -> Res.drawable.ic_moneysim
            "ic_medicine" -> Res.drawable.ic_medicine
            "ic_home_expenses" -> Res.drawable.ic_home_expenses
            "ic_groceries" -> Res.drawable.ic_groceries
            "ic_savings_pig" -> Res.drawable.ic_savings_pig
            "ic_savings" -> Res.drawable.ic_savings
            else -> Res.drawable.ic_food // Default icon
        }
    }

    private fun parseColorFromString(colorString: String): Color {
        return when (colorString) {
            "red" -> Color(0xFFFF0000)
            "blue" -> Color(0xFF0000FF)
            "green" -> Color(0xFF00FF00)
            "yellow" -> Color(0xFFFFFF00)
            "orange" -> Color(0xFFFFA500)
            "purple" -> Color(0xFF800080)
            "pink" -> Color(0xFFFFC0CB)
            "cyan" -> Color(0xFF00FFFF)
            "gray" -> Color(0xFF808080)
            "black" -> Color(0xFF000000)
            "white" -> Color(0xFFFFFFFF)
            "brown" -> Color(0xFF8B4513)
            "turquoise" -> Color(0xFF40E0D0)
            "lime" -> Color(0xFF32CD32)
            "gold" -> Color(0xFFFFD700)
            "silver" -> Color(0xFFC0C0C0)
            "lavender" -> Color(0xFFE6E6FA)
            "peach" -> Color(0xFFFFE5B4)
            "aquamarine" -> Color(0xFF7FFFD4)
            "coral" -> Color(0xFFFF7F50)
            else -> Color(0xFF007AFF) // Default blue color
        }
    }

    private fun getIconIdentifier(icon: DrawableResource): String {
        return when (icon) {
            Res.drawable.ic_gifts -> "ic_gifts"
            Res.drawable.ic_food -> "ic_food"
            Res.drawable.ic_transport -> "ic_transport"
            Res.drawable.ic_baby -> "ic_baby"
            Res.drawable.ic_work -> "ic_work"
            Res.drawable.ic_travel -> "ic_travel"
            Res.drawable.ic_entertainmentame -> "ic_entertainmentame"
            Res.drawable.ic_moneysim -> "ic_moneysim"
            Res.drawable.ic_medicine -> "ic_medicine"
            Res.drawable.ic_home_expenses -> "ic_home_expenses"
            Res.drawable.ic_groceries -> "ic_groceries"
            Res.drawable.ic_savings_pig -> "ic_savings_pig"
            Res.drawable.ic_savings -> "ic_savings"
            else -> "ic_food" // Default icon
        }
    }

    private fun getColorIdentifier(color: Color): String {
        return when (color) {
            Color(0xFFFF0000) -> "red"
            Color(0xFF0000FF) -> "blue"
            Color(0xFF00FF00) -> "green"
            Color(0xFFFFFF00) -> "yellow"
            Color(0xFFFFA500) -> "orange"
            Color(0xFF800080) -> "purple"
            Color(0xFFFFC0CB) -> "pink"
            Color(0xFF00FFFF) -> "cyan"
            Color(0xFF808080) -> "gray"
            Color(0xFF000000) -> "black"
            Color(0xFFFFFFFF) -> "white"
            Color(0xFF8B4513) -> "brown"
            Color(0xFF40E0D0) -> "turquoise"
            Color(0xFF32CD32) -> "lime"
            Color(0xFFFFD700) -> "gold"
            Color(0xFFC0C0C0) -> "silver"
            Color(0xFFE6E6FA) -> "lavender"
            Color(0xFFFFE5B4) -> "peach"
            Color(0xFF7FFFD4) -> "aquamarine"
            Color(0xFFFF7F50) -> "coral"
            else -> "blue" // Default color
        }
    }
}
