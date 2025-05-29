package com.tihcodes.finanzapp.co.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import compose.icons.TablerIcons
import compose.icons.tablericons.FileInfo

@Composable
fun InfoScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Información",
                notificationsCount = 0,
                showBackButton = true,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding) // Respetar el padding del Scaffold
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ListItem(
                    headlineContent = { Text("Términos y condiciones") },
                    leadingContent = {
                        androidx.compose.material3.Icon(
                            TablerIcons.FileInfo,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        // Navegar a pantalla de términos
                    }
                )

                ListItem(
                    headlineContent = { Text("Política de privacidad") },
                    leadingContent = {
                        androidx.compose.material3.Icon(
                            TablerIcons.FileInfo,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        // Navegar a pantalla de política de privacidad
                    }
                )
                ListItem(
                    headlineContent = { Text("Versión de la app") },
                    supportingContent = { Text("1.0.0") },
                    leadingContent = {
                        androidx.compose.material3.Icon(
                            TablerIcons.FileInfo,
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}