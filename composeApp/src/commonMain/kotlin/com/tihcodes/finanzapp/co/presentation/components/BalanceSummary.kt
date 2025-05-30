package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.utils.Validator.formatNumberWithCommas
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_more
import kotlinx.datetime.Clock
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@Composable
fun BalanceSummary(
    transactionRepository: TransactionRepository,
    userId: String
) {
    // Opciones del segmento
    val segments = listOf("Día", "Semana", "Mes", "Año")
    val (selectedSegment, setSelectedSegment) = remember { androidx.compose.runtime.mutableStateOf(0) }

    // Cargar datos persistidos
    val (persistedBalance, persistedExpenses) = remember {
        transactionRepository.getBalanceData(userId)
    }

    // Obtener transacciones
    val transactions by transactionRepository.transactions.collectAsState()

    // Filtrar transacciones según el segmento seleccionado
    val now = Clock.System.now()
        .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    val filteredTransactions = remember(transactions, selectedSegment) {
        val previousBalance = when (selectedSegment) {
            0, 1, 2 -> {
                // Para Día, Semana y Mes, sumar el saldo a favor de meses anteriores
                transactions.filter {
                    it.date.year < now.year ||
                            (it.date.year == now.year && it.date.monthNumber < now.monthNumber)
                }
                    .sumOf {
                        when (it.type) {
                            TransactionType.INCOME -> it.amount
                            TransactionType.EXPENSE -> -it.amount
                            TransactionType.BUDGET -> 0.0
                        }
                    }
            }
            else -> 0.0
        }
        val filtered = transactions.filter { transaction ->
            when (selectedSegment) {
                0 -> transaction.date == now // Día
                1 -> {
                    val startOfWeek = now.minus(
                        (now.dayOfWeek.ordinal).toLong(),
                        kotlinx.datetime.DateTimeUnit.DAY
                    )
                    transaction.date in startOfWeek..now
                }
                2 -> transaction.date.year == now.year && transaction.date.monthNumber == now.monthNumber // Mes
                3 -> transaction.date.year == now.year // Año
                else -> true
            }
        }
        if (previousBalance != 0.0) {
            // Agrega un "saldo anterior" ficticio al inicio de la lista
            listOf(
                com.tihcodes.finanzapp.co.domain.model.TransactionItem(
                    id = "saldo_anterior",
                    title = "Saldo anterior",
                    category = "",
                    date = now,
                    amount = previousBalance,
                    type = TransactionType.INCOME,
                    icon = Res.drawable.ic_more,
                    userId = userId
                )
            ) + filtered
        } else {
            filtered
        }
    }

    // Calcular balance y gastos filtrados
    val totalBalance = filteredTransactions.sumOf {
        when (it.type) {
            TransactionType.INCOME -> it.amount
            TransactionType.EXPENSE -> -it.amount
            TransactionType.BUDGET -> 0.0
        }
    }
    val totalExpenses = filteredTransactions.filter { it.type == TransactionType.EXPENSE }
        .sumOf { it.amount }

    // Guardar los valores calculados en el repositorio
    LaunchedEffect(totalBalance, totalExpenses, selectedSegment) {
        transactionRepository.saveBalanceData(userId, totalBalance, totalExpenses)
    }

    Column {
        // Segmented control animado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            segments.forEachIndexed { index, label ->
                val isSelected = selectedSegment == index
                androidx.compose.animation.AnimatedContent(
                    targetState = isSelected,
                    label = ""
                ) { selected ->
                    Text(
                        text = label,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                            )
                            .clickable { setSelectedSegment(index) }
                            .padding(horizontal = 18.dp, vertical = 8.dp),
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sección del balance total
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                    .padding(12.dp)
                    .width(160.dp)
            ) {
                Text(
                    text = "Total Balance",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "$${
                        if (filteredTransactions.isEmpty()) formatNumberWithCommas(
                            persistedBalance
                        ) else formatNumberWithCommas(totalBalance)
                    }",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            // Sección de gastos totales
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                    .padding(12.dp)
                    .width(160.dp)
            ) {
                Text(
                    text = "Total Expense",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (filteredTransactions.isEmpty()) {
                        formatNumberWithCommas(persistedExpenses)
                    } else {
                        formatNumberWithCommas(totalExpenses)
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}