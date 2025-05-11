package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.utils.getIconIdentifier
import com.tihcodes.finanzapp.co.utils.parseIconFromString
import com.tihcodes.finanzapp.co.utils.parseTransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlin.random.Random

class TransactionDatabase(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = Database(
        driver = databaseDriverFactory.createDriver()
    )
    private val transactionQueries = database.transactionQueries

    suspend fun getAllTransactions(): List<TransactionItem> = withContext(Dispatchers.Default) {
        println("INFO: Reading all transactions from database")
        val transactions = transactionQueries.getAllTransactions().executeAsList()

        return@withContext transactions.map { transaction ->
            TransactionItem(
                id = transaction.id,
                title = transaction.title,
                category = transaction.category,
                date = parseDate(transaction.date),
                amount = transaction.amount,
                type = parseTransactionType(transaction.type),
                icon = parseIconFromString(transaction.icon),
                userId = transaction.userId
            )
        }
    }

    suspend fun getTransactionsByUserId(userId: String): List<TransactionItem> = withContext(Dispatchers.Default) {
        println("INFO: Reading transactions for user $userId from database")
        val transactions = transactionQueries.getTransactionsByUserId(userId).executeAsList()

        return@withContext transactions.map { transaction ->
            TransactionItem(
                id = transaction.id,
                title = transaction.title,
                category = transaction.category,
                date = parseDate(transaction.date),
                amount = transaction.amount,
                type = parseTransactionType(transaction.type),
                icon = parseIconFromString(transaction.icon),
                userId = transaction.userId
            )
        }
    }

    suspend fun getTransactionsByCategory(category: String, userId: String): List<TransactionItem> = withContext(Dispatchers.Default) {
        println("INFO: Reading transactions for category $category and user $userId from database")
        val transactions = transactionQueries.getTransactionsByCategory(category, userId).executeAsList()

        return@withContext transactions.map { transaction ->
            TransactionItem(
                id = transaction.id,
                title = transaction.title,
                category = transaction.category,
                date = parseDate(transaction.date),
                amount = transaction.amount,
                type = parseTransactionType(transaction.type),
                icon = parseIconFromString(transaction.icon),
                userId = transaction.userId
            )
        }
    }

    suspend fun getTransactionsByType(type: TransactionType, userId: String): List<TransactionItem> = withContext(Dispatchers.Default) {
        println("INFO: Reading transactions of type $type for user $userId from database")
        val transactions = transactionQueries.getTransactionsByType(type.name, userId).executeAsList()

        return@withContext transactions.map { transaction ->
            TransactionItem(
                id = transaction.id,
                title = transaction.title,
                category = transaction.category,
                date = parseDate(transaction.date),
                amount = transaction.amount,
                type = parseTransactionType(transaction.type),
                icon = parseIconFromString(transaction.icon),
                userId = transaction.userId
            )
        }
    }

    suspend fun getTransactionById(id: String): TransactionItem? = withContext(Dispatchers.Default) {
        println("INFO: Reading transaction with id $id from database")
        val transaction = transactionQueries.getTransactionById(id).executeAsOneOrNull() ?: return@withContext null

        return@withContext TransactionItem(
            id = transaction.id,
            title = transaction.title,
            category = transaction.category,
            date = parseDate(transaction.date),
            amount = transaction.amount,
            type = parseTransactionType(transaction.type),
            icon = parseIconFromString(transaction.icon),
            userId = transaction.userId
        )
    }

    suspend fun insertTransaction(transaction: TransactionItem) = withContext(Dispatchers.Default) {
        println("INFO: Inserting transaction into database")
        transactionQueries.insertTransaction(
            id = transaction.id.ifEmpty { generateId() },
            title = transaction.title,
            category = transaction.category,
            date = formatDate(transaction.date),
            amount = transaction.amount,
            type = transaction.type.name,
            icon = getIconIdentifier(transaction.icon),
            userId = transaction.userId
        )
    }

    suspend fun updateTransaction(transaction: TransactionItem) = withContext(Dispatchers.Default) {
        println("INFO: Updating transaction in database")
        transactionQueries.updateTransaction(
            id = transaction.id,
            title = transaction.title,
            category = transaction.category,
            date = formatDate(transaction.date),
            amount = transaction.amount,
            type = transaction.type.name,
            icon = getIconIdentifier(transaction.icon),
            userId = transaction.userId
        )
    }

    suspend fun deleteTransaction(id: String) = withContext(Dispatchers.Default) {
        println("INFO: Deleting transaction from database")
        transactionQueries.deleteTransaction(id)
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
}