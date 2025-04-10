package com.tihcodes.finanzapp.co.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Text(
        text = "Qué hubo pues!",
        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
        modifier = androidx.compose.ui.Modifier.padding(16.dp)
    )
}