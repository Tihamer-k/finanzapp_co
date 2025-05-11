package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.domain.model.CategoryItem
import com.tihcodes.finanzapp.co.utils.getColorIdentifier
import com.tihcodes.finanzapp.co.utils.getIconIdentifier
import com.tihcodes.finanzapp.co.utils.getSampleCategories
import com.tihcodes.finanzapp.co.utils.parseColorFromString
import com.tihcodes.finanzapp.co.utils.parseIconFromString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
        val category =
            categoryQueries.getCategoryById(id).executeAsOneOrNull() ?: return@withContext null

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

    suspend fun updateCategory(id: String, category: CategoryItem) =
        withContext(Dispatchers.Default) {
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
        return categoryQueries.getCategoryByNameAndUserId(name, userId).executeAsOneOrNull()
            ?.let { category ->
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


}


