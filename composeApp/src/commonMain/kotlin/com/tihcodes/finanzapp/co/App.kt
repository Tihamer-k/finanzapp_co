package com.tihcodes.finanzapp.co

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.ui.Navigation
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import com.tihcodes.finanzapp.co.ui.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    Theme {
        val authViewModel = koinViewModel<AuthViewModel>()
        val surfaceColor = MaterialTheme.colorScheme.background
        val authStateValue = authViewModel.currentUser.collectAsState(initial = null)
        val authState = authStateValue.value?.id?.isEmpty()
        println("authState: $authState")

        Surface(color = surfaceColor) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (authState) {
                    null -> ShowLoadingIndicator()
                    else -> {
                        val destination = getNavigationDestination(authState)
                        Navigation(authViewModel = authViewModel, destination = destination)
                    }
                }
            }
        }
    }
}

/** Helper function to show a loading indicator */
@Composable
private fun ShowLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .padding(16.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

/** Determine navigation destination based on authentication state */
private fun getNavigationDestination(authState: Any?): String {
    return if (authState == true) "onboarding" else "home"
}