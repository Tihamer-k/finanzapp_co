package com.tihcodes.finanzapp.co.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.utils.Validator
import finanzapp_co.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.material3.Button
import finanzapp_co.composeapp.generated.resources.ic_google


@Composable
fun ForgotPasswordScreen(
    onBackToLogin: () -> Unit,
    onRegister: () -> Unit,
    onContinue: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Olvidaste Tú Contraseña",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Olvidaste Tú Contraseña?\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ingresa Tú Correo",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Start)
        )

        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            placeholder = { Text("example@example.com") },
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
                if (Validator.isEmailValid(email)) {
                    onContinue(email)
                } else {
                    emailError = "Correo inválido"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .height(48.dp)
        ) {
            Text("Siguiente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onRegister() }) {
            Text("Regístrate", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("O inicia sesión con", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(8.dp))

        Icon(
            painter = painterResource(Res.drawable.ic_google), // Tu ícono definido
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("¿Ya tienes una cuenta?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = onBackToLogin) {
                Text("Inicia sesión", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
