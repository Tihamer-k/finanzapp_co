package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.data.local.CategoryDatabase
import com.tihcodes.finanzapp.co.domain.model.CategoryItem
import com.tihcodes.finanzapp.co.utils.getColorIdentifier
import com.tihcodes.finanzapp.co.utils.getIconIdentifier
import com.tihcodes.finanzapp.co.utils.getSampleCategories
import com.tihcodes.finanzapp.co.utils.parseColorFromString
import com.tihcodes.finanzapp.co.utils.parseIconFromString
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Repository for managing categories
 * Uses in-memory storage with Firestore synchronization
 */
class CategoryRepository(
    private val firestore: FirebaseFirestore? = null,
    private val categoryDatabase: CategoryDatabase? = null,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {


    private val _categories = MutableStateFlow<List<CategoryItem>>(emptyList())
    val categories: StateFlow<List<CategoryItem>> = _categories.asStateFlow()

    private var isInitialized = false
    private var currentUserId: String = ""

    /**
     * Initialize the repository with sample categories if empty
     */
    fun initialize(userId: String = "") {
        println("[DEBUG_LOG] Initializing CategoryRepository with userId: $userId")
        println("[DEBUG_LOG] Current state: isInitialized=$isInitialized, currentUserId=$currentUserId")

        if (!isInitialized || currentUserId != userId) {
            currentUserId = userId
            println("[DEBUG_LOG] Need to initialize categories for userId: $userId")

            // Try to load categories from local database first
            scope.launch {
                try {
                    if (categoryDatabase != null) {
                        // Get categories from SQLDelight
                        println("[DEBUG_LOG] Getting categories from SQLDelight")
                        val localCategories = categoryDatabase.getAllCategories()
                        println("[DEBUG_LOG] Found ${localCategories.size} categories in SQLDelight")

                        if (localCategories.isNotEmpty()) {
                            // If we have local categories, use them
                            println("[DEBUG_LOG] Using ${localCategories.size} categories from SQLDelight")
                            _categories.value = localCategories
                            isInitialized = true
                            println("[DEBUG_LOG] Categories initialized from SQLDelight")
                            return@launch
                        }
                    }

                    // If no local categories or database not available, use sample categories
                    println("[DEBUG_LOG] No local categories found, using sample categories")
                    val sampleCategories = if (userId.isNotEmpty()) {
                        println("[DEBUG_LOG] Creating sample categories with userId: $userId")
                        getSampleCategories().map { it.copy(userId = userId) }
                    } else {
                        println("[DEBUG_LOG] Creating sample categories with empty userId")
                        getSampleCategories()
                    }
                    _categories.value = sampleCategories
                    println("[DEBUG_LOG] Set ${sampleCategories.size} sample categories")

                    // Save sample categories to local database if available
                    if (categoryDatabase != null && userId.isNotEmpty()) {
                        println("[DEBUG_LOG] Saving sample categories to SQLDelight")
                        sampleCategories.forEach { category ->
                            categoryDatabase.insertCategory(category)
                        }
                        println("[DEBUG_LOG] Saved ${sampleCategories.size} sample categories to SQLDelight")
                    }

                    isInitialized = true
                    println("[DEBUG_LOG] Categories initialized with sample data")
                } catch (e: Exception) {
                    println("[DEBUG_LOG] ERROR: Failed to initialize categories: ${e.message}")
                    e.printStackTrace()
                    // Fallback to sample categories
                    val sampleCategories = if (userId.isNotEmpty()) {
                        println("[DEBUG_LOG] Fallback: Creating sample categories with userId: $userId")
                        getSampleCategories().map { it.copy(userId = userId) }
                    } else {
                        println("[DEBUG_LOG] Fallback: Creating sample categories with empty userId")
                        getSampleCategories()
                    }
                    _categories.value = sampleCategories
                    isInitialized = true
                    println("[DEBUG_LOG] Categories initialized with fallback sample data")
                }
            }
        } else {
            println("[DEBUG_LOG] Categories already initialized for userId: $userId")
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
        println("[DEBUG_LOG] Starting syncCategories for userId: $userId")

        if (userId.isEmpty()) {
            println("[DEBUG_LOG] Empty userId, skipping sync")
            return
        }

        try {
            println("[DEBUG_LOG] Syncing categories for user: $userId")

            // Get default categories with user ID
            println("[DEBUG_LOG] Getting default categories with userId: $userId")
            val defaultCategories = getSampleCategories().map { it.copy(userId = userId) }
            println("[DEBUG_LOG] Got ${defaultCategories.size} default categories")

            // Start with default categories
            val mergedCategories = defaultCategories.toMutableList()
            println("[DEBUG_LOG] Starting with ${mergedCategories.size} default categories")

            // If SQLDelight is available, get categories from it
            if (categoryDatabase != null) {
                try {
                    println("[DEBUG_LOG] Getting categories from SQLDelight")
                    val localCategories = categoryDatabase.getAllCategories()
                    println("[DEBUG_LOG] Found ${localCategories.size} categories in SQLDelight")

                    // Add local categories that don't exist in default categories
                    var localCategoriesAdded = 0
                    localCategories.forEach { localCategory ->
                        if (localCategory.userId == userId) {
                            val existingIndex = mergedCategories.indexOfFirst { it.name == localCategory.name }
                            if (existingIndex >= 0) {
                                // Replace existing category
                                println("[DEBUG_LOG] Replacing existing category: ${localCategory.name}")
                                mergedCategories[existingIndex] = localCategory
                            } else {
                                // Add new category
                                println("[DEBUG_LOG] Adding new local category: ${localCategory.name}")
                                mergedCategories.add(localCategory)
                                localCategoriesAdded++
                            }
                        }
                    }

                    println("[DEBUG_LOG] Added $localCategoriesAdded new categories from SQLDelight")
                } catch (e: Exception) {
                    println("[DEBUG_LOG] ERROR: Failed to get categories from SQLDelight: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                println("[DEBUG_LOG] SQLDelight database is null, skipping local sync")
            }

            // If Firestore is available, get categories from it
            if (firestore != null) {
                try {
                    println("[DEBUG_LOG] Syncing categories from Firestore for user: $userId")

                    // Get categories from Firestore
                    println("[DEBUG_LOG] Querying Firestore for categories")
                    val firestoreCategories = firestore.collection("users")
                        .document(userId)
                        .collection("categories")
                        .get()
                    println("[DEBUG_LOG] Firestore query completed, processing documents")

                    val userCategories = mutableListOf<CategoryItem>()

                    // Convert Firestore documents to CategoryItems
                    println("[DEBUG_LOG] Processing ${firestoreCategories.documents.size} Firestore documents")
                    firestoreCategories.documents.forEach { doc ->
                        try {
                            val name = doc.get("name") as? String
                            if (name == null) {
                                println("[DEBUG_LOG] Skipping document with null name")
                                return@forEach
                            }

                            val nameReference = doc.get("nameReference") as? String ?: name
                            val iconString = doc.get("icon") as? String
                            val colorString = doc.get("backgroundColor") as? String

                            println("[DEBUG_LOG] Processing Firestore category: $name")

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
                            println("[DEBUG_LOG] Added Firestore category: $name")
                        } catch (e: Exception) {
                            println("[DEBUG_LOG] ERROR: Failed to parse category from Firestore: ${e.message}")
                            e.printStackTrace()
                        }
                    }

                    println("[DEBUG_LOG] Retrieved ${userCategories.size} categories from Firestore")

                    // Add user categories that don't exist in merged categories
                    var firestoreCategoriesAdded = 0
                    userCategories.forEach { userCategory ->
                        val existingIndex = mergedCategories.indexOfFirst { it.name == userCategory.name }
                        if (existingIndex >= 0) {
                            // Replace existing category
                            println("[DEBUG_LOG] Replacing existing category with Firestore data: ${userCategory.name}")
                            mergedCategories[existingIndex] = userCategory
                        } else {
                            // Add new category
                            println("[DEBUG_LOG] Adding new Firestore category: ${userCategory.name}")
                            mergedCategories.add(userCategory)
                            firestoreCategoriesAdded++
                        }
                    }
                    println("[DEBUG_LOG] Added $firestoreCategoriesAdded new categories from Firestore")
                } catch (e: Exception) {
                    println("[DEBUG_LOG] ERROR: Failed to sync categories from Firestore: ${e.message}")
                    e.printStackTrace()
                }
            } else {
                println("[DEBUG_LOG] Firestore is null, skipping remote sync")
            }

            // Update the categories state
            println("[DEBUG_LOG] Updating categories state with ${mergedCategories.size} categories")
            _categories.value = mergedCategories
            isInitialized = true
            currentUserId = userId
            println("[DEBUG_LOG] Categories state updated, isInitialized=$isInitialized, currentUserId=$currentUserId")

            // Save merged categories to SQLDelight if available
            if (categoryDatabase != null) {
                try {
                    println("[DEBUG_LOG] Saving merged categories to SQLDelight")
                    // Get existing categories from the database
                    val existingCategories = categoryDatabase.getAllCategories()
                    println("[DEBUG_LOG] Found ${existingCategories.size} existing categories in SQLDelight")

                    // Only insert categories that don't already exist
                    val categoriesToInsert = mergedCategories.filter { newCategory ->
                        // Check if a category with the same name and userId already exists
                        !existingCategories.any { existingCategory ->
                            existingCategory.name == newCategory.name && existingCategory.userId == newCategory.userId
                        }
                    }
                    println("[DEBUG_LOG] Need to insert ${categoriesToInsert.size} new categories to SQLDelight")

                    // Insert new categories
                    categoriesToInsert.forEach { category ->
                        println("[DEBUG_LOG] Inserting category to SQLDelight: ${category.name}")
                        categoryDatabase.insertCategory(category)
                    }

                    println("[DEBUG_LOG] Saved ${categoriesToInsert.size} new categories to SQLDelight")
                } catch (e: Exception) {
                    println("[DEBUG_LOG] ERROR: Failed to save categories to SQLDelight: ${e.message}")
                    e.printStackTrace()
                }
            }

            println("[DEBUG_LOG] Categories synced successfully. Total categories: ${mergedCategories.size}")
        } catch (e: Exception) {
            println("[DEBUG_LOG] ERROR: Failed to sync categories: ${e.message}")
            e.printStackTrace()
            // Fall back to default categories
            println("[DEBUG_LOG] Falling back to initialize method")
            initialize(userId)
        }
    }
}
