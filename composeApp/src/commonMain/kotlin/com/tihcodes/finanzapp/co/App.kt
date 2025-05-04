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
import com.tihcodes.finanzapp.co.presentation.components.Navigation
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.presentation.theme.Theme
import org.koin.compose.viewmodel.koinViewModel

private const val ONBOARDING_DESTINATION = "onboarding"
private const val HOME_DESTINATION = "home"

@Composable
fun App() {
    Theme {
        val authViewModel = koinViewModel<AuthViewModel>()
        val surfaceColor = MaterialTheme.colorScheme.background
        val authState = determineAuthState(authViewModel)

        Surface(color = surfaceColor) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (authState) {
                    null -> ShowLoadingIndicator()
                    else -> Navigation(
                        authViewModel = authViewModel,
                        destination = getNavigationDestination(authState)
                    )
                }
            }
        }
    }
}

/** Determine current authentication state */
@Composable
private fun determineAuthState(authViewModel: AuthViewModel): Boolean? {
    val collectedAuthState = authViewModel.currentUser.collectAsState(initial = null)
    return collectedAuthState.value?.id?.isNotEmpty()
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
private fun getNavigationDestination(authState: Boolean?): String {
    return if (authState == true) HOME_DESTINATION else ONBOARDING_DESTINATION
}