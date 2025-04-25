package com.tihcodes.finanzapp.co.ui.screens.modules.notifications

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.data.NotificationItem
import com.tihcodes.finanzapp.co.data.NotificationSection
import com.tihcodes.finanzapp.co.ui.TopNavBar
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_category

@Composable
fun NotificationsScreen(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Categorías",
                notificationsCount = 0,
                showBackButton = true,
            )
        },

        ) { paddingValues ->

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
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues) // Respetar el padding del Scaffold
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
    }
}
