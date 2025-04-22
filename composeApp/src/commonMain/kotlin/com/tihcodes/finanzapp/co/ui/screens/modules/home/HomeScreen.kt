package com.tihcodes.finanzapp.co.ui.screens.modules.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.User
import com.tihcodes.finanzapp.co.ui.BottomNavBar
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.screens.model.AuthViewModel


@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit,
    viewModel: AuthViewModel,
    navController: NavHostController
) {
    val isLoading = viewModel.isProcessing.collectAsState().value
    val user = viewModel.currentUser.collectAsState().value ?: User()

    LaunchedEffect(Unit) {
        viewModel.syncUserData()
    }

    if (isLoading) {
        // Mostrar un indicador de carga
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Mostrar la pantalla principal
        Scaffold(
            topBar = {
                TopNavBar(
                    navController = navController,
                    title = "Inicio",
                    notificationsCount = 3,
                    )
            },

            bottomBar = {
                BottomNavBar(
                    indexIn = 0,
                    onItemClick = navController
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    .padding(paddingValues)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(0.1f))

                    Text(
                        text = "Bienvenido, ${user.name}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
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
                    Spacer(Modifier.weight(0.3f))
                }
            }
        }
    }
}