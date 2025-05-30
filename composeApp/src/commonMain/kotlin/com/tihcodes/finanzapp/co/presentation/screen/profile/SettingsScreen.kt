package com.tihcodes.finanzapp.co.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.ui.theme.ThemeManager

@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Configuración",
                notificationsCount = 0,
                showBackButton = true,
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(padding)
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
                val isDarkTheme = remember { mutableStateOf(ThemeManager.isDarkTheme) }
                val fontSize = remember { mutableStateOf(ThemeManager.fontSize.value) }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Cambiar tema de la aplicación",
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = isDarkTheme.value,
                                onCheckedChange = {
                                    isDarkTheme.value = it
                                    ThemeManager.isDarkTheme = it
                                }
                            )
                        }
                    }

                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "Tamaño de fuente",
                                fontSize = 16.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                            Slider(
                                value = fontSize.value,
                                onValueChange = {
                                    fontSize.value = it
                                    ThemeManager.fontSize = it.sp
                                },
                                valueRange = 12f..24f
                            )
                            Text(
                                text = "Vista previa del texto",
                                fontSize = fontSize.value.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
