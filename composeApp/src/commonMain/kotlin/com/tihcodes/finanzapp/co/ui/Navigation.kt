package com.tihcodes.finanzapp.co.ui

import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tihcodes.finanzapp.co.data.TransactionType
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import com.tihcodes.finanzapp.co.ui.screens.auth.ForgotPasswordScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.LoginScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.PreLoginScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.SignUpScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.CategoryDetailScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.CategoryScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.NewCategoryScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.home.HomeScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.learn.LearnScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.notifications.NotificationsScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.profile.ProfileScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.records.NewTransactionScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.records.RecordsScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.rewards.RewardsScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.rewards.SimulatorScreen
import com.tihcodes.finanzapp.co.ui.screens.onboarding.Onboarding

@Composable
fun Navigation(authViewModel: AuthViewModel, destination: String) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = destination,
        enterTransition = {
            when (initialState.destination.route) {
                "onboarding" -> fadeIn()
                else -> fadeIn(initialAlpha = 1f)
            }
        }
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
                onRegisterClick = { navController.navigate("home") },
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
                onLoginClick = { navController.navigate("home") },
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
                onLogoutClick = { navController.navigate("pre-login") },
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
        composable("new_transaction_income") {
            NewTransactionScreen(navController, type = TransactionType.INCOME)
        }
        composable("new_transaction_expense") {
            NewTransactionScreen(navController, type = TransactionType.EXPENSE)
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
                onLogoutClick = { navController.navigate("pre-login") },
                viewModel = authViewModel,
                navController = navController
            )
        }

        composable("simulator/{simulatorName}") { backStack ->
            val name = backStack.arguments?.getString("simulatorName") ?: "Simulador"
            SimulatorScreen(
                simulatorName = name,
                navController = navController,
            ) 
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


    }

}
