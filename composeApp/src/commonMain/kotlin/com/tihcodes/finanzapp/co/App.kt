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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.presentation.components.Navigation
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.ui.theme.Theme
import com.tihcodes.finanzapp.co.ui.theme.ThemeManager
import org.koin.compose.viewmodel.koinViewModel

private const val ONBOARDING_DESTINATION = "onboarding"
private const val HOME_DESTINATION = "home"
private const val PRE_LOGIN_DESTINATION = "pre-login"

@Composable
fun App() {
    Theme(
        darkTheme = ThemeManager.isDarkTheme
    ) {
        val authViewModel = koinViewModel<AuthViewModel>()
        val surfaceColor = MaterialTheme.colorScheme.background
        val authState = determineAuthState(authViewModel)
        val isSignedOut = authViewModel.isSignedOut.collectAsState().value
        val showOnboarding = authViewModel.showOnboarding.collectAsState().value
        val isSignedIn = authViewModel.isSignIn.collectAsState().value

        // Sincroniza los datos del usuario al iniciar la app
        LaunchedEffect(Unit) {
            authViewModel.syncUserData()
        }

        Surface(color = surfaceColor) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (authState) {
                    null -> ShowLoadingIndicator()
                    else -> Navigation(
                        authViewModel = authViewModel,
                        destination = getNavigationDestination(authState, isSignedOut, showOnboarding, isSignedIn)
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
private fun getNavigationDestination(
    authState: Boolean?,
    isSignedOut: Boolean,
    showOnboarding: Boolean,
    isSignedIn: Boolean
): String {
    return when {
        authState!! && isSignedIn-> HOME_DESTINATION
        isSignedOut -> PRE_LOGIN_DESTINATION
        showOnboarding -> ONBOARDING_DESTINATION
        else -> {
            PRE_LOGIN_DESTINATION
        }
    }
}
