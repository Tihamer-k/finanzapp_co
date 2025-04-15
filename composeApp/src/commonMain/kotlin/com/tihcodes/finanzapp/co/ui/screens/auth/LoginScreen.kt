package com.tihcodes.finanzapp.co.ui.screens.auth

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.ui.screens.model.AuthViewModel
import com.tihcodes.finanzapp.co.utils.Validator
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_eye_close
import finanzapp_co.composeapp.generated.resources.ic_eye_open
import finanzapp_co.composeapp.generated.resources.ic_google
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource


@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    viewModel: AuthViewModel
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val authState by viewModel.authState.collectAsState()
    val isEmailValid = Validator.isEmailValid(uiState.email)
    val isPasswordValid = Validator.isPasswordValid(uiState.password).first
    val errorMessagePassword = Validator.isPasswordValid(uiState.password).second
    val isFormValid = isEmailValid && isPasswordValid
    var clicked by rememberSaveable { mutableStateOf(false) }

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
                .padding(vertical = 60.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(
            modifier = Modifier.weight(0.1f).fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)) // Mover el clip aquí
                .background(MaterialTheme.colorScheme.background)
        )

        // Email
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp),
            contentAlignment = Alignment.Center
        ) {


            OutlinedTextField(
                value = uiState.email.lowercase(),
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo") },
                placeholder = {
                    Text(
                        "example@example.com",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                              },
                isError = uiState.email.isNotEmpty() && !isEmailValid,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp),
            contentAlignment = Alignment.Center
        ) {
            // Password
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Contraseña") },
                placeholder = {
                    Text(
                        "********",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = if (!passwordVisible) painterResource(Res.drawable.ic_eye_close)
                            else painterResource(Res.drawable.ic_eye_open),
                            contentDescription = "Mostrar/Ocultar contraseña"
                        )
                    }
                },
                isError = uiState.password.isNotEmpty() && !isPasswordValid,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp),
            contentAlignment = Alignment.Center
        ) {

            if (!isPasswordValid && uiState.password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                if (errorMessagePassword != null) {
                    Text(
                        text = errorMessagePassword,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

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
            // Botón Login (solo habilitado si es válido)
            Button(
                onClick = {
                    viewModel.onSignInClick()
                    clicked = true
                },
                enabled = isFormValid,
                modifier = Modifier.padding(horizontal = 16.dp)
                    .size(width = 200.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Inicia Sesión",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp),
            contentAlignment = Alignment.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
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
                    onLoginClick()
                    clicked = false
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 60.dp),
            contentAlignment = Alignment.Center,
        ) {

            if (errorMessage.isNotEmpty()) {
                LaunchedEffect(errorMessage) {
                    delay(3000) // 3 segundos
                    errorMessage = ""
                }
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp),
            contentAlignment = Alignment.Center
        ) {

            TextButton(onClick = onForgotPasswordClick) {
                Text(
                    text = "¿Olvidaste tú contraseña?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    textDecoration = TextDecoration.Underline
                )
            }
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

            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .height(48.dp).padding(horizontal = 16.dp)
                    .size(width = 200.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(
                    text = "Regístrate",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(
            modifier = Modifier.height(16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
        )

//        Spacer(modifier = Modifier.height(16.dp))

//        Text(
//            text = "Usa Tú Huella Para Acceder",
//            style = MaterialTheme.typography.bodyMedium,
//            color = MaterialTheme.colorScheme.onBackground
//        )
        Box(
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
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 26.dp),
            contentAlignment = Alignment.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes una cuenta?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Regístrate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(0.1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
        )

    }
}
