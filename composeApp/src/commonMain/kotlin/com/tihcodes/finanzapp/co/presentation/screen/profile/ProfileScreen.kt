package com.tihcodes.finanzapp.co.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.User
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Settings
import compose.icons.tablericons.Shield

@Composable
fun ProfileScreen(
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    viewModel: AuthViewModel,
) {
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Perfil",
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
                    .padding(16.dp)
            ) {
                val user = viewModel.currentUser.collectAsState().value ?: User()

                Spacer(modifier = Modifier.height(16.dp))

                // Círculo con iniciales
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    val initials =
                        "${user.name.firstOrNull() ?: ""}${user.surname.firstOrNull() ?: ""}".uppercase()
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 32.sp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${user.name} ${user.surname}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(32.dp))

                ProfileOptionItem(icon = TablerIcons.Settings, label = "Configuración") {
                    // Navegar a pantalla de configuración
                    navController.navigate("settings")
                }
                ProfileOptionItem(icon = TablerIcons.Edit, label = "Editar Perfil") {
                    // Navegar a pantalla de edición
                    navController.navigate("edit_profile")
                }
                ProfileOptionItem(icon = TablerIcons.Shield, label = "Seguridad") {
                    // Navegar a términos y condiciones
                    navController.navigate("security")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.onSignOut()
                        onLogoutClick()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(width = 200.dp, height = 48.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = "Cerrar sesión",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

