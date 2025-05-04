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
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository

@Composable
fun BalanceSummary(
    transactionRepository: TransactionRepository,
    userId: String
) {
    // Cargar datos persistidos
    val (persistedBalance, persistedExpenses) = remember {
        transactionRepository.getBalanceData(userId)
    }

    // Calcular balance y gastos actuales
    val transactions by transactionRepository.transactions.collectAsState()
    val totalBalance = transactionRepository.calculateTotalBalance(userId)
    val totalExpenses = transactionRepository.calculateTotalExpenses(userId)

    // Guardar los valores calculados en el repositorio
    LaunchedEffect(totalBalance, totalExpenses) {
        transactionRepository.saveBalanceData(userId, totalBalance, totalExpenses)
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
                .width(140.dp)
                .clickable { }
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$${if (transactions.isEmpty()) persistedBalance else totalBalance}",
                style = MaterialTheme.typography.titleMedium,
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
                .width(140.dp)
                .clickable { }
        ) {
            Text(
                text = "Total Expense",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "-$${if (transactions.isEmpty()) persistedExpenses else totalExpenses}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
