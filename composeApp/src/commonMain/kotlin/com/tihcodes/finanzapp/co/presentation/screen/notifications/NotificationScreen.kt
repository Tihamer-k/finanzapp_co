package com.tihcodes.finanzapp.co.presentation.screen.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.viewmodel.AppNotificationViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import org.koin.compose.koinInject

@Composable
fun NotificationsScreen(
    navController: NavController,

) {
    val authViewModel = koinInject<AuthViewModel>()
    val viewModel = koinInject<AppNotificationViewModel>()
    val notifications by viewModel.notifications.collectAsState() // Observe notifications
    val unreadNotifications = notifications.filter { !it.isRead }
    val readNotifications = notifications.filter { it.isRead }
    val idUser = authViewModel.currentUser.value?.id ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadNotifications(idUser)
    }
    println("NotificationsScreen - Unread: ${unreadNotifications.size}, Read: ${readNotifications.size}")

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Notificaciones",
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
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Sección de notificaciones no leídas
                if (unreadNotifications.isNotEmpty()) {
                    Text(
                        text = "No leídas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(unreadNotifications) { notification ->
                            NotificationCard(notification = notification)
                            Button(
                                onClick = { viewModel.markAsRead(notification.id) },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(text = "Marcar como leída")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de notificaciones leídas
                if (readNotifications.isNotEmpty()) {
                    Text(
                        text = "Leídas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(readNotifications) { notification ->
                            NotificationCard(notification = notification)
                            Button(
                                onClick = { viewModel.removeNotification(notification.id) },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(text = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
