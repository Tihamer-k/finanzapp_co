package com.tihcodes.finanzapp.co.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.utils.Validator
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.pre_login_button_sign_up
import org.jetbrains.compose.resources.stringResource


@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onRegister: () -> Unit,
    onContinue: () -> Unit,
    viewModel: AuthViewModel,
    onGoogleLoginClick: () -> Unit
) {
    var emailError by remember { mutableStateOf<String?>(null) }
    var clicked by remember { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var successMessage by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(45.dp).fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Restablece tú contraseña",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = "Olvidaste Tú Contraseña?\nA continuación ingresa tu correo registrado para restablecerla.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Ingresa Tú Correo",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start).padding(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = uiState.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                    emailError = null
                },
                placeholder = {
                    Text(
                        "example@example.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

            emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (Validator.isEmailValid(uiState.email)) {
                        viewModel.onReset()
                        clicked = true
                    } else {
                        emailError = "Correo inválido"
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
                    .size(width = 200.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    "Siguiente",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            if (clicked) {
                if (isProcessing) {
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (!authState) {
                    errorMessage = "Error de autenticación usuario o contraseña incorrectos"
                    clicked = false
                } else if (authState) {
                    successMessage = "Correo enviado, revisa tu bandeja de entrada"
                    clicked = false
                }
            }
            errorMessage.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            successMessage.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Button(
                onClick = { onRegister() },
                modifier = Modifier.padding(horizontal = 16.dp)
                    .size(width = 200.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = stringResource(Res.string.pre_login_button_sign_up),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

  /*          Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 26.dp),
                contentAlignment = Alignment.Center
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "O inicia sesión con",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(
                modifier = Modifier.height(16.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 26.dp),
                contentAlignment = Alignment.Center
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                IconButton(onClick = onGoogleLoginClick) {
                    Image(
                        painter = painterResource(Res.drawable.ic_google),
                        contentDescription = "Iniciar con Google",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }*/

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ya tienes una cuenta?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onBackToLogin) {
                    Text(
                        text = "Inicia sesión",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}
