package com.tihcodes.finanzapp.co.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tihcodes.finanzapp.co.ui.screens.onboarding.Onboarding

@Composable
fun Navigation() {

    val navController = rememberNavController()
    val destination = "onboarding"

    NavHost(navController = navController, startDestination = destination) {

        composable("onboarding") {
            Onboarding(navController = navController)
        }
        composable("pre-login") {
            //PreLogin(navController = navController)
        }

    }

}

