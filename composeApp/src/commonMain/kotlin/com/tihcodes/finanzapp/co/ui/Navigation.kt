package com.tihcodes.finanzapp.co.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tihcodes.finanzapp.co.ui.screens.auth.ForgotPasswordScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.LoginScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.PreLoginScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.SignUpScreen
import com.tihcodes.finanzapp.co.ui.screens.model.AuthViewModel
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.CategoryScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.home.HomeScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.learn.LearnScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.notifications.NotificationsScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.records.RecordsScreen
import com.tihcodes.finanzapp.co.ui.screens.modules.rewards.RewardsScreen
import com.tihcodes.finanzapp.co.ui.screens.onboarding.Onboarding

@Composable
fun Navigation(authViewModel: AuthViewModel) {

    val navController = rememberNavController()
    val isSignIn = authViewModel.isSignIn.collectAsState().value
    val destination = "pre-login"
    // val destination = if (!isSignIn) "onboarding" else "home"

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
                onLoginClick =  { navController.navigate("home") },
                onGoogleLoginClick = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgot-password") },
                viewModel = authViewModel,
            )
        }
        composable("home") {
            HomeScreen(
                onLogoutClick = { navController.navigate("pre-login") },
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
                onLogoutClick = { navController.navigate("pre-login") },
                viewModel = authViewModel,
                navController = navController
            )
        }
        composable("categories") {
            CategoryScreen(
                onLogoutClick = { navController.navigate("pre-login") },
                viewModel = authViewModel,
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

        composable("notifications") {
            NotificationsScreen(
                navController = navController
            )
        }





    }

}

