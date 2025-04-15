package com.tihcodes.finanzapp.co.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.ui.screens.model.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel, onLogoutClick: () -> Unit) {

    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Tú Id: ${viewModel.currentUser.value?.id}",
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onBackground,
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        )
        Button(
            onClick = {
                viewModel.onSignOut()
                onLogoutClick()
            },
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        ) {
            Text(text = "Logout")
        }
    }
}