package com.tihcodes.finanzapp.co.ui.screens.modules.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.TransactionType
import com.tihcodes.finanzapp.co.ui.TopNavBar

@Composable
fun NewTransactionScreen(
    navController: NavHostController,
    type: TransactionType
) {
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = type.name,
                notificationsCount = 0,
                showBackButton = true,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Agregar nueva transacción",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { /* Guardar transacción */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Guardar")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
