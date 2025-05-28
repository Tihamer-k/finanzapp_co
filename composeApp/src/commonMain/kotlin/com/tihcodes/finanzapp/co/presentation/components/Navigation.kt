package com.tihcodes.finanzapp.co.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.screen.auth.ForgotPasswordScreen
import com.tihcodes.finanzapp.co.presentation.screen.auth.LoginScreen
import com.tihcodes.finanzapp.co.presentation.screen.auth.PreLoginScreen
import com.tihcodes.finanzapp.co.presentation.screen.auth.SignUpScreen
import com.tihcodes.finanzapp.co.presentation.screen.categories.CategoryDetailScreen
import com.tihcodes.finanzapp.co.presentation.screen.categories.CategoryScreen
import com.tihcodes.finanzapp.co.presentation.screen.categories.NewCategoryScreen
import com.tihcodes.finanzapp.co.presentation.screen.home.HomeScreen
import com.tihcodes.finanzapp.co.presentation.screen.learn.CourseContentScreen
import com.tihcodes.finanzapp.co.presentation.screen.learn.CoursesModule
import com.tihcodes.finanzapp.co.presentation.screen.learn.LearnScreen
import com.tihcodes.finanzapp.co.presentation.screen.notifications.NotificationsScreen
import com.tihcodes.finanzapp.co.presentation.screen.onboarding.Onboarding
import com.tihcodes.finanzapp.co.presentation.screen.profile.ProfileScreen
import com.tihcodes.finanzapp.co.presentation.screen.records.NewTransactionScreen
import com.tihcodes.finanzapp.co.presentation.screen.records.RecordsScreen
import com.tihcodes.finanzapp.co.presentation.screen.rewards.RewardsScreen
import com.tihcodes.finanzapp.co.presentation.screen.simulators.FinanceSimulatorScreen
import com.tihcodes.finanzapp.co.presentation.screen.simulators.SimulatorScreen
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import org.koin.compose.koinInject

@Composable
fun Navigation(authViewModel: AuthViewModel, destination: String) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = destination,
    ) {

        composable("onboarding") {
            Onboarding(navController = navController)
        }
        composable("pre-login") {
            PreLoginScreen(navController = navController)
        }
        composable("register") {
            SignUpScreen(
                viewModel = authViewModel,
                onRegisterClick = { navController.navigate("login") {
                    popUpTo("pre-login") { inclusive = true }
                } },
                onLoginNavigate = { navController.navigate("login") },
            )
        }
        composable("forgot-password") {
            ForgotPasswordScreen(
                onBackToLogin = { navController.navigate("login") },
                onContinue = { navController.navigate("login") },
                onRegister = { navController.navigate("register") },
                onGoogleLoginClick = { navController.navigate("home") },
                viewModel = authViewModel,
            )
        }
        composable("login") {
            LoginScreen(
                onLoginClick = { navController.navigate("home") {
                    popUpTo("pre-login") { inclusive = true }
                } },
                onGoogleLoginClick = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgot-password") },
                viewModel = authViewModel,
            )
        }
        composable("home") {
            HomeScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable("learn") {
            LearnScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable("records") {
            RecordsScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable(
            "new_transaction_income?userId={userId}&category={category}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val transactionRepository = koinInject<TransactionRepository>()
            val categoryRepository = koinInject<CategoryRepository>()
            NewTransactionScreen(
                navController = navController,
                type = TransactionType.INCOME,
                transactionRepository = transactionRepository,
                categoryRepository = categoryRepository,
                userId = userId,
                preSelectedCategory = category
            )
        }
        composable(
            "new_transaction_expense?userId={userId}&category={category}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val transactionRepository = koinInject<TransactionRepository>()
            val categoryRepository = koinInject<CategoryRepository>()
            NewTransactionScreen(
                navController = navController,
                type = TransactionType.EXPENSE,
                transactionRepository = transactionRepository,
                categoryRepository = categoryRepository,
                userId = userId,
                preSelectedCategory = category
            )
        }
        composable(
            "new_transaction_budget?userId={userId}&category={category}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                },
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val transactionRepository = koinInject<TransactionRepository>()
            val categoryRepository = koinInject<CategoryRepository>()
            NewTransactionScreen(
                navController = navController,
                type = TransactionType.BUDGET,
                transactionRepository = transactionRepository,
                categoryRepository = categoryRepository,
                userId = userId,
                preSelectedCategory = category
            )
        }

        composable("categories") {
            CategoryScreen(
                onLogoutClick = { navController.navigate("pre-login") },
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable(
            "categoryDetail/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryDetailScreen(
                categoryName = categoryName,
                navController = navController
            )
        }

        composable("rewards") {
            RewardsScreen(
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable("simulator/{simulatorName}/{simulatorId}") { backStack ->
            val name = backStack.arguments?.getString("simulatorName") ?: "Simulador"
            val id = backStack.arguments?.getString("simulatorId") ?: "0"
            when (id) {
                "sim_1" -> {
                    SimulatorScreen(
                        simulatorName = name,
                        navController = navController,
                    )
                }

                "sim_2" -> {
                    FinanceSimulatorScreen(
                        simulatorName = name,
                        navController = navController,
                    )
                }

                else -> {
                    SimulatorScreen(
                        simulatorName = name,
                        navController = navController,
                    )
                }
            }
        }

        composable("notifications") {
            NotificationsScreen(
                navController = navController
            )
        }
        composable("profile") {
            ProfileScreen(
                onLogoutClick = { navController.navigate("pre-login") },
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable("new_category") {
            NewCategoryScreen(
                navController = navController
            )
        }

        composable(
            "course/{courseId}/isCompleted={isCompleted}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("isCompleted") {
                    type = NavType.BoolType
                    defaultValue = false
                })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val isCompleted = backStackEntry.arguments?.getBoolean("isCompleted") ?: false
            CourseContentScreen(
                courseId = courseId,
                isCompleted = isCompleted,
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable(
            "course-content/{courseId}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            CoursesModule(
                courseId = courseId,
                viewModel = authViewModel,
                navController = navController,
            )
        }

    }

}
