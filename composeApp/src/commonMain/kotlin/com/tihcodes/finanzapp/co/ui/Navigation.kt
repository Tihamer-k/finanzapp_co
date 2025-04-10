package com.tihcodes.finanzapp.co.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tihcodes.finanzapp.co.ui.screens.HomeScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.ForgotPasswordScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.LoginScreen
import com.tihcodes.finanzapp.co.ui.screens.onboarding.Onboarding
import com.tihcodes.finanzapp.co.ui.screens.auth.PreLoginScreen
import com.tihcodes.finanzapp.co.ui.screens.auth.SignUpScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val destination = "onboarding"

    NavHost(navController = navController, startDestination = destination) {

        composable("onboarding") {
            Onboarding(navController = navController)
        }
        composable("pre-login") {
            PreLoginScreen(navController = navController)
        }
        composable("register") {
            SignUpScreen(
                onRegisterClick = { navController.navigate("home") },
                onLoginNavigate = { navController.navigate("login") },
            )
        }
        composable("forgot-password") {
            ForgotPasswordScreen(
                onBackToLogin = { navController.navigate("login") },
                onContinue = { navController.navigate("login") },
                onRegister = { navController.navigate("register") },
            )
        }
        composable("login") {
            LoginScreen(
                onLoginClick =  { navController.navigate("home") },
                onGoogleLoginClick = { navController.navigate("home") },
                onRegisterClick = { navController.navigate("register") },
                onForgotPasswordClick = { navController.navigate("forgot-password") }
            )
        }
        composable("home") {
            HomeScreen()
        }

    }

}

