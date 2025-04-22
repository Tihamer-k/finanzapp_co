package com.tihcodes.finanzapp.co.ui.screens.modules.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.data.NotificationItem
import com.tihcodes.finanzapp.co.data.NotificationSection
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_calendar
import finanzapp_co.composeapp.generated.resources.ic_category
import finanzapp_co.composeapp.generated.resources.ic_notifications
import org.jetbrains.compose.resources.painterResource

@Composable
fun NotificationsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val insets = WindowInsets.systemBars.asPaddingValues()

    val exampleNotifications = listOf(
        NotificationSection(
            title = "Hoy",
            items = listOf(
                NotificationItem(
                    id = "1",
                    icon = Res.drawable.ic_category,
                    title = "Recordatorio!",
                    message = "In a laoreet purus. Integer turpis quam, laoreet id at nec...",
                    dateTime = "12/10/2023 12:00",
                ),
                NotificationItem(
                    id = "2",
                    icon = Res.drawable.ic_category,
                    title = "Nueva Recompensa!",
                    message = "Vestibulum eu quam nec neque pellentesque...",
                    dateTime = "12/10/2023 12:00"
                )
            )
        ),
        NotificationSection(
            title = "Yesterday",
            items = listOf(
                NotificationItem(
                    id = "3",
                    icon = Res.drawable.ic_category,
                    title = "Transacciones",
                    message = "Mercado | Comida | -$100,00",
                    categoryTag = "Mercado | Comida | -$100,00",
                    categoryColor = MaterialTheme.colorScheme.primary,
                    dateTime = "12/10/2023 12:00",
                )
            )
        )
    )

    val sections = exampleNotifications

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .padding(top = insets.calculateTopPadding())
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // TopBar centrada visualmente con Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            // Botón Back
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_calendar), // reemplaza con tu ícono de flecha
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Título
            Text(
                text = "Notificación",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(section.items) { notification ->
                    NotificationCard(notification = notification)
                }
            }
        }
    }
}
