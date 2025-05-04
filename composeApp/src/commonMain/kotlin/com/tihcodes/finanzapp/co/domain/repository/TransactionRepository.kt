package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.data.local.TransactionDatabase
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import dev.gitlive.firebase.firestore.FirebaseFirestore
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
import kotlin.random.Random

/**
 * Repository for managing transactions (incomes, expenses, and budgets)
 * Uses in-memory storage with Firestore synchronization
 */
class TransactionRepository(
    private val firestore: FirebaseFirestore? = null,
    private val transactionDatabase: TransactionDatabase? = null,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) {

    private val _transactions = MutableStateFlow<List<TransactionItem>>(emptyList())
    val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

    private var isInitialized = false
    private var currentUserId: String = ""

    // Simulación de almacenamiento local (puedes usar Firestore, Room, etc.)
    private val balanceData = mutableMapOf<String, Pair<Double, Double>>() // userId -> (totalBalance, totalExpenses)

    fun saveBalanceData(userId: String, totalBalance: Double, totalExpenses: Double) {
        balanceData[userId] = Pair(totalBalance, totalExpenses)
    }

    fun getBalanceData(userId: String): Pair<Double, Double> {
        return balanceData[userId] ?: Pair(0.0, 0.0) // Valores predeterminados si no hay datos
    }

    /**
     * Initialize the repository with sample transactions if empty
     */
    fun initialize(userId: String = "") {
        if (!isInitialized || currentUserId != userId) {
            currentUserId = userId

            // Start with empty transactions
            _transactions.value = emptyList()

            // Try to load transactions from local database first
            scope.launch {
                try {
                    if (transactionDatabase != null && userId.isNotEmpty()) {
                        // Get transactions from SQLDelight
                        val localTransactions = transactionDatabase.getTransactionsByUserId(userId)

                        if (localTransactions.isNotEmpty()) {
                            // If we have local transactions, use them
                            _transactions.value = localTransactions
                            isInitialized = true
                            return@launch
                        }
                    }

                    // Sync with Firestore if available
                    if (userId.isNotEmpty() && firestore != null) {
                        syncTransactions(userId)
                    }

                    isInitialized = true
                } catch (e: Exception) {
                    println("ERROR: Failed to initialize transactions: ${e.message}")
                    isInitialized = true
                }
            }
        }
    }

    /**
     * Get all transactions
     * Filters by userId if provided
     */
    fun getAllTransactions(userId: String = ""): List<TransactionItem> {
        if (!isInitialized || currentUserId != userId) {
            initialize(userId)
        }

        return if (userId.isNotEmpty()) {
            _transactions.value.filter { it.userId == userId }
        } else {
            _transactions.value
        }
    }

    /**
     * Get transactions by type (income, expense, budget)
     * Filters by userId if provided
     */
    fun getTransactionsByType(type: TransactionType, userId: String = ""): List<TransactionItem> {
        if (!isInitialized || currentUserId != userId) {
            initialize(userId)
        }

        return if (userId.isNotEmpty()) {
            _transactions.value.filter { it.type == type && it.userId == userId }
        } else {
            _transactions.value.filter { it.type == type }
        }
    }

    /**
     * Get transactions by category
     * Filters by userId if provided
     */
    fun getTransactionsByCategory(category: String, userId: String = ""): List<TransactionItem> {
        if (!isInitialized || currentUserId != userId) {
            initialize(userId)
        }

        return if (userId.isNotEmpty()) {
            _transactions.value.filter { it.category == category && it.userId == userId }
        } else {
            _transactions.value.filter { it.category == category }
        }
    }

    /**
     * Add a new transaction and save to both SQLDelight and Firestore if available
     */
    fun addTransaction(transaction: TransactionItem) {
        // Generate ID if not provided
        val transactionWithId = if (transaction.id.isEmpty()) {
            transaction.copy(id = generateId())
        } else {
            transaction
        }

        // Update in-memory cache
        _transactions.update { currentList ->
            currentList + transactionWithId
        }

        scope.launch {
            try {
                // Save to SQLDelight if available
                if (transactionDatabase != null) {
                    transactionDatabase.insertTransaction(transactionWithId)
                    println("INFO: Transaction saved to SQLDelight: ${transactionWithId.id}")
                }

                // Save to Firestore if available
                if (firestore != null && transaction.userId.isNotEmpty()) {
                    try {
                        val transactionData = mapOf(
                            "id" to transactionWithId.id,
                            "title" to transactionWithId.title,
                            "category" to transactionWithId.category,
                            "date" to formatDate(transactionWithId.date),
                            "amount" to transactionWithId.amount,
                            "type" to transactionWithId.type.name,
                            "icon" to getIconIdentifier(transactionWithId.icon)
                            // Removed userId field as it's redundant - it's already in the document path
                        )

                        // Use userId/transactions collection to store user-specific transactions
                        firestore.collection("users")
                            .document(transaction.userId)
                            .collection("transactions")
                            .document(transactionWithId.id)
                            .set(transactionData)

                        println("INFO: Transaction saved to Firestore: ${transactionWithId.id}")
                    } catch (e: Exception) {
                        println("ERROR: Failed to save transaction to Firestore: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("ERROR: Failed to save transaction: ${e.message}")
            }
        }
    }

    /**
     * Update an existing transaction in both SQLDelight and Firestore
     */
    fun updateTransaction(transaction: TransactionItem) {
        // Update in-memory cache
        _transactions.update { currentList ->
            currentList.map { 
                if (it.id == transaction.id && it.userId == transaction.userId) transaction else it 
            }
        }

        scope.launch {
            try {
                // Update in SQLDelight if available
                if (transactionDatabase != null) {
                    transactionDatabase.updateTransaction(transaction)
                    println("INFO: Transaction updated in SQLDelight: ${transaction.id}")
                }

                // Update in Firestore if available
                if (firestore != null && transaction.userId.isNotEmpty()) {
                    try {
                        val transactionData = mapOf(
                            "id" to transaction.id,
                            "title" to transaction.title,
                            "category" to transaction.category,
                            "date" to formatDate(transaction.date),
                            "amount" to transaction.amount,
                            "type" to transaction.type.name,
                            "icon" to getIconIdentifier(transaction.icon)
                        )

                        // Update existing document
                        firestore.collection("users")
                            .document(transaction.userId)
                            .collection("transactions")
                            .document(transaction.id)
                            .update(transactionData)

                        println("INFO: Transaction updated in Firestore: ${transaction.id}")
                    } catch (e: Exception) {
                        println("ERROR: Failed to update transaction in Firestore: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("ERROR: Failed to update transaction: ${e.message}")
            }
        }
    }

    /**
     * Delete a transaction from both SQLDelight and Firestore
     */
    fun deleteTransaction(transaction: TransactionItem) {
        // Update in-memory cache
        _transactions.update { currentList ->
            currentList.filter { it.id != transaction.id || it.userId != transaction.userId }
        }

        scope.launch {
            try {
                // Delete from SQLDelight if available
                if (transactionDatabase != null) {
                    transactionDatabase.deleteTransaction(transaction.id)
                    println("INFO: Transaction deleted from SQLDelight: ${transaction.id}")
                }

                // Delete from Firestore if available
                if (firestore != null && transaction.userId.isNotEmpty()) {
                    try {
                        firestore.collection("users")
                            .document(transaction.userId)
                            .collection("transactions")
                            .document(transaction.id)
                            .delete()

                        println("INFO: Transaction deleted from Firestore: ${transaction.id}")
                    } catch (e: Exception) {
                        println("ERROR: Failed to delete transaction from Firestore: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                println("ERROR: Failed to delete transaction: ${e.message}")
            }
        }
    }

    /**
     * Sync transactions with Firestore for a specific user
     */
    suspend fun syncTransactions(userId: String) {
        if (userId.isEmpty() || firestore == null) {
            return
        }

        try {
            println("INFO: Syncing transactions from Firestore for user: $userId")

            // Get transactions from Firestore
            val firestoreTransactions = firestore.collection("users")
                .document(userId)
                .collection("transactions")
                .get()

            val userTransactions = mutableListOf<TransactionItem>()

            // Convert Firestore documents to TransactionItems
            firestoreTransactions.documents.forEach { doc ->
                try {
                    val id = doc.get("id") as? String ?: return@forEach
                    val title = doc.get("title") as? String ?: ""
                    val category = doc.get("category") as? String ?: ""
                    val dateString = doc.get("date") as? String ?: ""
                    val amount = (doc.get("amount") as? String) ?: "0.0"
                    val typeString = doc.get("type") as? String ?: ""
                    val iconString = doc.get("icon") as? String ?: ""

                    // Parse date from string
                    val date = parseDate(dateString)

                    // Parse type from string
                    val type = parseTransactionType(typeString)

                    // Parse icon from string
                    val icon = parseIconFromString(iconString)

                    // Convert amount to Double
                    val amountDouble = try {
                        amount.toDouble()
                    } catch (e: NumberFormatException) {
                        0.0 // Default value if parsing fails
                    }

                    // Create the transaction with the parsed values
                    userTransactions.add(
                        TransactionItem(
                            id = id,
                            title = title,
                            category = category,
                            date = date,
                            amount = amountDouble,
                            type = type,
                            icon = icon,
                            userId = userId
                        )
                    )
                } catch (e: Exception) {
                    println("ERROR: Failed to parse transaction from Firestore: ${e.message}")
                }
            }

            println("INFO: Retrieved ${userTransactions.size} transactions from Firestore")

            // Update the transactions state
            _transactions.value = userTransactions
            isInitialized = true
            currentUserId = userId

            // Save synced transactions to SQLDelight if available
            if (transactionDatabase != null) {
                try {
                    // Save each transaction to SQLDelight
                    userTransactions.forEach { transaction ->
                        transactionDatabase.insertTransaction(transaction)
                    }
                    println("INFO: Saved ${userTransactions.size} transactions to SQLDelight")
                } catch (e: Exception) {
                    println("ERROR: Failed to save transactions to SQLDelight: ${e.message}")
                }
            }

            println("INFO: Transactions synced successfully. Total transactions: ${userTransactions.size}")
        } catch (e: Exception) {
            println("ERROR: Failed to sync transactions: ${e.message}")
        }
    }

    /**
     * Calculate total balance for a user
     */
    fun calculateTotalBalance(userId: String): Double {
        val userTransactions = getAllTransactions(userId)

        return userTransactions.sumOf { transaction ->
            when (transaction.type) {
                TransactionType.INCOME -> transaction.amount
                TransactionType.EXPENSE -> -transaction.amount
                TransactionType.BUDGET -> 0.0 // Budgets don't affect the balance
            }
        }
    }

    /**
     * Calculate total expenses for a user
     */
    fun calculateTotalExpenses(userId: String): Double {
        val expenses = getTransactionsByType(TransactionType.EXPENSE, userId)
        return expenses.sumOf { it.amount }
    }

    /**
     * Calculate total income for a user
     */
    fun calculateTotalIncome(userId: String): Double {
        val incomes = getTransactionsByType(TransactionType.INCOME, userId)
        return incomes.sumOf { it.amount }
    }

    /**
     * Calculate total budget for a user
     */
    fun calculateTotalBudget(userId: String): Double {
        val budgets = getTransactionsByType(TransactionType.BUDGET, userId)
        return budgets.sumOf { it.amount }
    }

    private fun generateId(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun parseDate(dateString: String): LocalDate {
        val parts = dateString.split("-")
        return if (parts.size == 3) {
            try {
                LocalDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
            } catch (e: Exception) {
                LocalDate(2024, 1, 1) // Default date
            }
        } else {
            LocalDate(2024, 1, 1) // Default date
        }
    }

    private fun formatDate(date: LocalDate): String {
        return "${date.year}-${date.monthNumber}-${date.dayOfMonth}"
    }

    private fun parseTransactionType(typeString: String): TransactionType {
        return when (typeString) {
            "INCOME" -> TransactionType.INCOME
            "EXPENSE" -> TransactionType.EXPENSE
            "BUDGET" -> TransactionType.BUDGET
            else -> TransactionType.EXPENSE // Default type
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
}
