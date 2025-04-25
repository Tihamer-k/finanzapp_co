package com.tihcodes.finanzapp.co.ui.screens.modules.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.User
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_user
import org.jetbrains.compose.resources.painterResource

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
                val user = viewModel.currentUser.collectAsState().value ?: User()

                Spacer(modifier = Modifier.weight(0.1f))
                // Imagen de perfil
                Image(
                    painter = painterResource(Res.drawable.ic_user), // Usa el ícono de perfil o una imagen personalizada
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .align(Alignment.CenterHorizontally)

                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre de usuario
                Text(
                    text = "${user.name} ${user.surname}", // Reemplaza por el nombre real del usuario
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                )

                // Correo electrónico
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.email, // Reemplaza con el correo electrónico real
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Espacio
                Spacer(modifier = Modifier.weight(1f))

                // Botón de cerrar sesión
                Button(
                    onClick = {
                        viewModel.onSignOut()
                        onLogoutClick()
                    }, modifier = Modifier.padding(horizontal = 16.dp)
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
