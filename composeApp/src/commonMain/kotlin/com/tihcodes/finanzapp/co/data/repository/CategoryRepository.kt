package com.tihcodes.finanzapp.co.data.repository

import androidx.compose.ui.graphics.Color
import com.tihcodes.finanzapp.co.data.CategoryItem
import com.tihcodes.finanzapp.co.data.local.CategoryDatabase
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.getSampleCategories
import dev.gitlive.firebase.firestore.FirebaseFirestore
import finanzapp_co.composeapp.generated.resources.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

/**
 * Repository for managing categories
 * Uses in-memory storage with Firestore synchronization
 */
class CategoryRepository(
    private val firestore: FirebaseFirestore? = null,
    private val categoryDatabase: CategoryDatabase? = null,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    /**
     * Get the identifier for an icon
     * @param icon The DrawableResource to get the identifier for
     * @return The string identifier for the icon
     */
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

    /**
     * Parse icon from string representation
     * @param iconString The string representation of the icon
     * @return The DrawableResource corresponding to the icon string, or a default icon if parsing fails
     */
    private fun parseIconFromString(iconString: String?): DrawableResource {
        if (iconString == null) return Res.drawable.ic_food // Default icon

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

    /**
     * Get the identifier for a color
     * @param color The Color to get the identifier for
     * @return The string identifier for the color
     */
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

    /**
     * Parse color from string representation
     * @param colorString The string representation of the color
     * @return The Color corresponding to the color string, or a default color if parsing fails
     */
    private fun parseColorFromString(colorString: String?): Color {
        if (colorString == null) return Color(0xFF007AFF) // Default blue color

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
    private val _categories = MutableStateFlow<List<CategoryItem>>(emptyList())
    val categories: StateFlow<List<CategoryItem>> = _categories.asStateFlow()

    private var isInitialized = false
    private var currentUserId: String = ""

    /**
     * Initialize the repository with sample categories if empty
     */
    fun initialize(userId: String = "") {
        if (!isInitialized || currentUserId != userId) {
            currentUserId = userId

            // Try to load categories from local database first
            scope.launch {
                try {
                    if (categoryDatabase != null) {
                        // Get categories from SQLDelight
                        val localCategories = categoryDatabase.getAllCategories()

                        if (localCategories.isNotEmpty()) {
                            // If we have local categories, use them
                            _categories.value = localCategories
                            isInitialized = true
                            return@launch
                        }
                    }

                    // If no local categories or database not available, use sample categories
                    val sampleCategories = if (userId.isNotEmpty()) {
                        getSampleCategories().map { it.copy(userId = userId) }
                    } else {
                        getSampleCategories()
                    }
                    _categories.value = sampleCategories

                    // Save sample categories to local database if available
                    if (categoryDatabase != null && userId.isNotEmpty()) {
                        sampleCategories.forEach { category ->
                            categoryDatabase.insertCategory(category)
                        }
                    }

                    isInitialized = true
                } catch (e: Exception) {
                    println("ERROR: Failed to initialize categories: ${e.message}")
                    // Fallback to sample categories
                    val sampleCategories = if (userId.isNotEmpty()) {
                        getSampleCategories().map { it.copy(userId = userId) }
                    } else {
                        getSampleCategories()
                    }
                    _categories.value = sampleCategories
                    isInitialized = true
                }
            }
        }
    }

    /**
     * Get all categories, using sample categories as fallback if empty
     * Filters by userId if provided
     */
    fun getAllCategories(userId: String = ""): List<CategoryItem> {
        if (!isInitialized || currentUserId != userId) {
            initialize(userId)
        }

        return if (userId.isNotEmpty()) {
            _categories.value.filter { it.userId == userId || it.userId.isEmpty() }
        } else {
            _categories.value
        }
    }

    /**
     * Add a new category and save to both SQLDelight and Firestore if available
     */
    fun addCategory(category: CategoryItem) {
        // Update in-memory cache
        _categories.update { currentList ->
            currentList + category
        }

        scope.launch {
            try {
                // Save to SQLDelight if available
                if (categoryDatabase != null) {
                    categoryDatabase.insertCategory(category)
                    println("INFO: Category saved to SQLDelight: ${category.name}")
                }

                // Save to Firestore if available
                if (firestore != null && category.userId.isNotEmpty()) {
                    try {
                        val categoryData = mapOf(
                            "name" to category.name,
                            "icon" to getIconIdentifier(category.icon),
                            "backgroundColor" to getColorIdentifier(category.backgroundColor),
                            "nameReference" to category.nameReference
                            // Removed userId field as it's redundant - it's already in the document path
                        )

                        // Use userId/categories collection to store user-specific categories
                        firestore.collection("users")
                            .document(category.userId)
                            .collection("categories")
                            .document(category.name)
                            .set(categoryData)

                        println("INFO: Category saved to Firestore: ${category.name}")
                    } catch (e: Exception) {
                        println("ERROR: Failed to save category to Firestore: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("ERROR: Failed to save category: ${e.message}")
            }
        }
    }

    /**
     * Update an existing category in both SQLDelight and Firestore
     */
    fun updateCategory(oldCategory: CategoryItem, newCategory: CategoryItem) {
        // Update in-memory cache
        _categories.update { currentList ->
            currentList.map { 
                if (it.name == oldCategory.name && it.userId == oldCategory.userId) newCategory else it 
            }
        }

        scope.launch {
            try {
                // Update in SQLDelight if available
                if (categoryDatabase != null) {
                    // We need to find the ID of the category in SQLDelight
                    // For now, we'll use the name as the ID since we're using it as the document ID in Firestore
                    categoryDatabase.updateCategory(oldCategory.name, newCategory)
                    println("INFO: Category updated in SQLDelight: ${newCategory.name}")
                }

                // Update in Firestore if available
                if (firestore != null && newCategory.userId.isNotEmpty()) {
                    try {
                        val categoryData = mapOf(
                            "name" to newCategory.name,
                            "icon" to getIconIdentifier(newCategory.icon),
                            "backgroundColor" to getColorIdentifier(newCategory.backgroundColor),
                            "nameReference" to newCategory.nameReference
                        )

                        // If the name changed, we need to delete the old document and create a new one
                        if (oldCategory.name != newCategory.name) {
                            // Delete old document
                            firestore.collection("users")
                                .document(oldCategory.userId)
                                .collection("categories")
                                .document(oldCategory.name)
                                .delete()

                            // Create new document
                            firestore.collection("users")
                                .document(newCategory.userId)
                                .collection("categories")
                                .document(newCategory.name)
                                .set(categoryData)
                        } else {
                            // Update existing document
                            firestore.collection("users")
                                .document(newCategory.userId)
                                .collection("categories")
                                .document(newCategory.name)
                                .update(categoryData)
                        }

                        println("INFO: Category updated in Firestore: ${newCategory.name}")
                    } catch (e: Exception) {
                        println("ERROR: Failed to update category in Firestore: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("ERROR: Failed to update category: ${e.message}")
            }
        }
    }

    /**
     * Delete a category from both SQLDelight and Firestore
     */
    fun deleteCategory(category: CategoryItem) {
        // Update in-memory cache
        _categories.update { currentList ->
            currentList.filter { it.name != category.name || it.userId != category.userId }
        }

        scope.launch {
            try {
                // Delete from SQLDelight if available
                if (categoryDatabase != null) {
                    // We need to find the ID of the category in SQLDelight
                    // For now, we'll use the name as the ID since we're using it as the document ID in Firestore
                    categoryDatabase.deleteCategory(category.name)
                    println("INFO: Category deleted from SQLDelight: ${category.name}")
                }

                // Delete from Firestore if available
                if (firestore != null && category.userId.isNotEmpty()) {
                    try {
                        firestore.collection("users")
                            .document(category.userId)
                            .collection("categories")
                            .document(category.name)
                            .delete()

                        println("INFO: Category deleted from Firestore: ${category.name}")
                    } catch (e: Exception) {
                        println("ERROR: Failed to delete category from Firestore: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("ERROR: Failed to delete category: ${e.message}")
            }
        }
    }

    /**
     * Get a category by name
     * Filters by userId if provided
     * Tries to get from in-memory cache first, then from SQLDelight if available
     */
    fun getCategoryByName(name: String, userId: String = ""): CategoryItem? {
        // First try to get from in-memory cache
        val cachedCategory = if (userId.isNotEmpty()) {
            _categories.value.find { it.name == name && (it.userId == userId || it.userId.isEmpty()) }
        } else {
            _categories.value.find { it.name == name }
        }

        if (cachedCategory != null) {
            return cachedCategory
        }

        // If not found in cache and SQLDelight is available, try to get from SQLDelight
        if (categoryDatabase != null) {
            scope.launch {
                try {
                    // We need to find the category by name in SQLDelight
                    // For now, we'll use the name as the ID since we're using it as the document ID in Firestore
                    val category = categoryDatabase.getCategoryById(name)
                    if (category != null && (userId.isEmpty() || category.userId == userId)) {
                        // Update in-memory cache
                        _categories.update { currentList ->
                            currentList + category
                        }
                    }
                } catch (e: Exception) {
                    println("ERROR: Failed to get category from SQLDelight: ${e.message}")
                }
            }
        }

        // Return null if not found
        return null
    }

    /**
     * Sync categories with Firestore and SQLDelight for a specific user
     * Retrieves categories from Firestore and merges them with default categories
     * Also saves the merged categories to SQLDelight
     */
    suspend fun syncCategories(userId: String) {
        if (userId.isEmpty()) {
            return
        }

        try {
            println("INFO: Syncing categories for user: $userId")

            // Get default categories with user ID
            val defaultCategories = getSampleCategories().map { it.copy(userId = userId) }

            // Start with default categories
            val mergedCategories = defaultCategories.toMutableList()

            // If SQLDelight is available, get categories from it
            if (categoryDatabase != null) {
                try {
                    val localCategories = categoryDatabase.getAllCategories()

                    // Add local categories that don't exist in default categories
                    localCategories.forEach { localCategory ->
                        if (localCategory.userId == userId) {
                            val existingIndex = mergedCategories.indexOfFirst { it.name == localCategory.name }
                            if (existingIndex >= 0) {
                                // Replace existing category
                                mergedCategories[existingIndex] = localCategory
                            } else {
                                // Add new category
                                mergedCategories.add(localCategory)
                            }
                        }
                    }

                    println("INFO: Retrieved ${localCategories.size} categories from SQLDelight")
                } catch (e: Exception) {
                    println("ERROR: Failed to get categories from SQLDelight: ${e.message}")
                }
            }

            // If Firestore is available, get categories from it
            if (firestore != null) {
                try {
                    println("INFO: Syncing categories from Firestore for user: $userId")

                    // Get categories from Firestore
                    val firestoreCategories = firestore.collection("users")
                        .document(userId)
                        .collection("categories")
                        .get()

                    val userCategories = mutableListOf<CategoryItem>()

                    // Convert Firestore documents to CategoryItems
                    firestoreCategories.documents.forEach { doc ->
                        try {
                            val name = doc.get("name") as? String ?: return@forEach
                            val nameReference = doc.get("nameReference") as? String ?: name
                            val iconString = doc.get("icon") as? String
                            val colorString = doc.get("backgroundColor") as? String

                            // Parse icon from string
                            val icon = parseIconFromString(iconString)

                            // Parse color from string
                            val backgroundColor = parseColorFromString(colorString)

                            // Create the category with the parsed values or fallbacks
                            userCategories.add(
                                CategoryItem(
                                    name = name,
                                    icon = icon,
                                    backgroundColor = backgroundColor,
                                    nameReference = nameReference,
                                    userId = userId
                                )
                            )
                        } catch (e: Exception) {
                            println("ERROR: Failed to parse category from Firestore: ${e.message}")
                        }
                    }

                    println("INFO: Retrieved ${userCategories.size} categories from Firestore")

                    // Add user categories that don't exist in merged categories
                    userCategories.forEach { userCategory ->
                        val existingIndex = mergedCategories.indexOfFirst { it.name == userCategory.name }
                        if (existingIndex >= 0) {
                            // Replace existing category
                            mergedCategories[existingIndex] = userCategory
                        } else {
                            // Add new category
                            mergedCategories.add(userCategory)
                        }
                    }
                } catch (e: Exception) {
                    println("ERROR: Failed to sync categories from Firestore: ${e.message}")
                }
            }

            // Update the categories state
            _categories.value = mergedCategories
            isInitialized = true
            currentUserId = userId

            // Save merged categories to SQLDelight if available
            if (categoryDatabase != null) {
                try {
                    // Get existing categories from the database
                    val existingCategories = categoryDatabase.getAllCategories()

                    // Only insert categories that don't already exist
                    val categoriesToInsert = mergedCategories.filter { newCategory ->
                        // Check if a category with the same name and userId already exists
                        !existingCategories.any { existingCategory ->
                            existingCategory.name == newCategory.name && existingCategory.userId == newCategory.userId
                        }
                    }

                    // Insert new categories
                    categoriesToInsert.forEach { category ->
                        categoryDatabase.insertCategory(category)
                    }

                    println("INFO: Saved ${categoriesToInsert.size} new categories to SQLDelight")
                } catch (e: Exception) {
                    println("ERROR: Failed to save categories to SQLDelight: ${e.message}")
                }
            }

            println("INFO: Categories synced successfully. Total categories: ${mergedCategories.size}")
        } catch (e: Exception) {
            println("ERROR: Failed to sync categories: ${e.message}")
            // Fall back to default categories
            initialize(userId)
        }
    }
}
