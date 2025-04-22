package com.tihcodes.finanzapp.co.ui.screens.modules.rewards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.ui.BottomNavBar
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel

@Composable
fun RewardsScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {

    Scaffold(
        bottomBar = {
            BottomNavBar(
                indexIn = 4,
                onItemClick = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Recompensas",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = viewModel.currentUser.value?.id?.let { "Tú Id: $it" }
                    ?: "Cargando usuario...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    viewModel.onSignOut()
                    onLogoutClick()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Logout")
            }
        }

    }
}
