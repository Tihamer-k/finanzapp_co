package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
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
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource
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