package com.tihcodes.finanzapp.co.presentation.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.User
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.components.BalanceSummary
import com.tihcodes.finanzapp.co.presentation.components.BottomNavBar
import com.tihcodes.finanzapp.co.presentation.components.ExpandableFab
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.components.TransactionPieChartWithKoalaPlot
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.CourseTrackingViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.NotificationViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.TransactionChartViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreen(
    viewModel: AuthViewModel,
    navController: NavHostController
) {
    val isLoading = viewModel.isProcessing.collectAsState().value
    val user = viewModel.currentUser.collectAsState().value ?: User()
    val listState = rememberLazyListState()
    val transactionRepository = koinInject<TransactionRepository>()
    val categoryRepository = koinInject<CategoryRepository>()
    val chartViewModel = koinInject<TransactionChartViewModel>()
    val userId = user.id
    val courseTracking = koinInject<CourseTrackingViewModel>()
    val notificationViewModel = koinViewModel<NotificationViewModel>()


    // Detectar si deberíamos mostrar el FAB
    val isFabVisible by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset == 0 ||
                    listState.isScrollInProgress.not()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.syncUserData()
    }
    LaunchedEffect(userId) {
        // First initialize with default categories
        categoryRepository.initialize(userId)
        transactionRepository.initialize(userId)

        // Then sync with Firestore to get user-specific data
        if (userId.isNotEmpty()) {
            categoryRepository.syncCategories(userId)
            transactionRepository.syncTransactions(userId)
            chartViewModel.setUserId(userId)
        }
    }

    if (isLoading) {
        // Mostrar un indicador de carga
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Mostrar la pantalla principal
        Scaffold(
            topBar = {
                TopNavBar(
                    navController = navController,
                    title = "Inicio",
                    notificationsCount = 3,
                    showBackButton = false,
                )
            },

            bottomBar = {
                BottomNavBar(
                    indexIn = 0,
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
                        },
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
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(MaterialTheme.colorScheme.background)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Bienvenido, ${user.name.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    BalanceSummary(
                        transactionRepository = transactionRepository,
                        userId = userId
                    )
                    Text(
                        text = "Visualiza los porcentajes",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                    TransactionPieChartWithKoalaPlot(
                        viewModel = chartViewModel,
                        notificationViewModel = notificationViewModel,
                    )
                    Spacer(modifier = Modifier.height(36.dp))

                    Spacer(Modifier.weight(0.3f))
                }
            }
        }
    }
}
