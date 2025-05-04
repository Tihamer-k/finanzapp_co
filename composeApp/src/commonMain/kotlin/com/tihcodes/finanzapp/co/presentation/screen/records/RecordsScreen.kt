package com.tihcodes.finanzapp.co.presentation.screen.records

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.components.BottomNavBar
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.components.BalanceSummary
import com.tihcodes.finanzapp.co.presentation.components.ExpandableFab
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecordsScreen(
    viewModel: AuthViewModel,
    navController: NavHostController,
    transactionRepository: TransactionRepository = koinInject(),
    categoryRepository: CategoryRepository = koinInject()
) {
    val transactionsGrouped by viewModel.filteredTransactions.collectAsState(initial = emptyMap<String, List<TransactionItem>>())
    val filterType by viewModel.filterType.collectAsState()
    val listState = rememberLazyListState()
    val currentUser by viewModel.currentUser.collectAsState()
    val userId = currentUser?.id ?: ""
    val finalTransactions by viewModel.filteredFinalTransactions.collectAsState(initial = emptyMap<String, List<TransactionItem>>())
    // Check if we're showing example data
    var realTransactions by remember { mutableStateOf(emptyList<TransactionItem>()) }
    realTransactions = transactionRepository.getAllTransactions(userId)

    // Detectar si deberíamos mostrar el FAB
    val isFabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset == 0 ||
                    listState.isScrollInProgress.not()
        }
    }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Registros",
                notificationsCount = 0,
                showBackButton = false,
            )
        },
        bottomBar = {
            BottomNavBar(
                indexIn = 2,
                onItemClick = navController
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFabVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ExpandableFab(
                    onAddIncome = {
                        navController.navigate("new_transaction_income?userId=$userId")
                    },
                    onAddExpense = {
                        navController.navigate("new_transaction_expense?userId=$userId")
                    },
                    onAddBudget = {
                        navController.navigate("new_transaction_budget?userId=$userId")
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // Balance Summary
                BalanceSummary(
                    transactionRepository = transactionRepository,
                    userId = userId
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Balance
                CardBalanceSection(
                    transactionRepository = transactionRepository,
                    userId = userId
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Filtro botones
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = Int.MAX_VALUE
                ) {
                    FilterButton(
                        label = "Todos",
                        selected = filterType == null,
                        onClick = { viewModel.setFilter(null) }
                    )
                    FilterButton(
                        label = "Ingresos",
                        selected = filterType == TransactionType.INCOME,
                        onClick = { viewModel.setFilter(TransactionType.INCOME) }
                    )
                    FilterButton(
                        label = "Gastos",
                        selected = filterType == TransactionType.EXPENSE,
                        onClick = { viewModel.setFilter(TransactionType.EXPENSE) }
                    )
                    FilterButton(
                        label = "Presupuestos",
                        selected = filterType == TransactionType.BUDGET,
                        onClick = { viewModel.setFilter(TransactionType.BUDGET) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                // Check and clear example data if needed
                viewModel.checkAndClearExampleData(realTransactions)

                val isShowingExampleData =
                    realTransactions.isEmpty() && transactionsGrouped.isNotEmpty()

                // Show example data label if needed
                if (isShowingExampleData) {
                    Text(
                        text = "Estos son ejemplos de registros. Se eliminarán cuando agregues tus propios registros.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Lista de transacciones agrupadas
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        transactionsGrouped.forEach { entry ->
                            val month = entry.key
                            val transactions = entry.value

                            item {
                                Text(
                                    text = month,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            items(transactions) { transaction ->
                                TransactionItemCard(transaction)
                            }
                        }
                    }
                } else {
                    // Lista de transacciones agrupadas
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        finalTransactions.forEach { entry ->
                            val month = entry.key
                            val transactions = entry.value

                            item {
                                Text(
                                    text = month,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            items(transactions) { transaction ->
                                TransactionItemCard(transaction)
                            }
                        }
                    }
                }
            }

        }
    }
}
