package com.tihcodes.finanzapp.co.ui.screens.modules.records

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.TransactionType
import com.tihcodes.finanzapp.co.ui.BottomNavBar
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.components.ExpandableFab
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel

@Composable
fun RecordsScreen(
    viewModel: AuthViewModel,
    navController: NavHostController
) {
    val transactionsGrouped by viewModel.filteredTransactions.collectAsState()
    val filterType by viewModel.filterType.collectAsState()
    val listState = rememberLazyListState()

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
                        navController.navigate("new_transaction_income")
                    },
                    onAddExpense = {
                        navController.navigate("new_transaction_expense")
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

                // Balance
                CardBalanceSection()

                Spacer(modifier = Modifier.height(24.dp))

                // Filtro botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de transacciones agrupadas
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    transactionsGrouped.forEach { (month, transactions) ->
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
