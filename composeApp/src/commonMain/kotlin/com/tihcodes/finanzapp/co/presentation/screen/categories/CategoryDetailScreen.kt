package com.tihcodes.finanzapp.co.presentation.screen.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.components.ExpandableFab
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.screen.records.TransactionItemCard
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.utils.getSampleCategories
import org.koin.compose.koinInject


@Composable
fun CategoryDetailScreen(
    categoryName: String,
    navController: NavHostController
) {
    val categoryRepository = koinInject<CategoryRepository>()
    val transactionRepository = koinInject<TransactionRepository>()
    val authViewModel = koinInject<AuthViewModel>()

    // Get current user ID from AuthViewModel
    val currentUser = authViewModel.currentUser.collectAsState().value
    val userId = currentUser?.id ?: ""
    val finalTransactions by authViewModel.filteredFinalTransactions.collectAsState(initial = emptyMap<String, List<TransactionItem>>())

    // Initialize repositories if needed with user ID and sync with Firestore
    LaunchedEffect(userId) {
        // First initialize with default categories
        categoryRepository.initialize(userId)
        transactionRepository.initialize(userId)

        // Then sync with Firestore to get user-specific data
        if (userId.isNotEmpty()) {
            categoryRepository.syncCategories(userId)
            transactionRepository.syncTransactions(userId)
        }
    }

    // Get category from repository or fallback to sample categories
    val categoryItem = remember(userId) {
        categoryRepository.getCategoryByName(categoryName, userId)
            ?: getSampleCategories().find { it.name == categoryName }?.copy(userId = userId)
    }

    // Get transactions for this category
    val allTransactions by transactionRepository.transactions.collectAsState()
    val categoryTransactions by remember { mutableStateOf(allTransactions.filter { it.category == categoryName }) }
    val listState = rememberLazyListState()


    // Check if we're showing example data
    authViewModel.setFinalTransactions(categoryTransactions)

    // Show/hide FAB
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
                title = categoryName,
                showBackButton = true // Nuevo parámetro para mostrar el botón de back
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
                        // Navigate to add income with pre-selected category and user ID
                        navController.navigate("new_transaction_income?category=$categoryName&userId=$userId")
                    },
                    onAddExpense = {
                        // Navigate to add expense with pre-selected category and user ID
                        navController.navigate("new_transaction_expense?category=$categoryName&userId=$userId")
                    },
                    onAddBudget = {
                        // Navigate to add budget with pre-selected category and user ID
                        navController.navigate("new_transaction_budget?category=$categoryName&userId=$userId")
                    }
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    categoryItem?.let { category ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Categoría: ${category.name}",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            if (categoryTransactions.isEmpty()) {
                                Text(
                                    text = "No hay transacciones para esta categoría",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                // Display transactions in a list
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

        }
    )
}


