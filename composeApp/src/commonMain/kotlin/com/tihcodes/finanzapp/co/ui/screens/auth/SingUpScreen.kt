package com.tihcodes.finanzapp.co.ui.screens.auth

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.ui.components.SuccessDialog
import com.tihcodes.finanzapp.co.ui.components.ConfettiAnimation
import com.tihcodes.finanzapp.co.utils.Validator
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_eye_close
import org.jetbrains.compose.resources.painterResource

@Composable
fun SignUpScreen(
    onRegisterClick: () -> Unit,
    onLoginNavigate: () -> Unit
) {
    // Estados locales para inputs
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showConfetti by rememberSaveable { mutableStateOf(false) }

    val isEmailValid = Validator.isEmailValid(email)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Crea Una Cuenta",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        val modifierField = Modifier
            .fillMaxWidth(0.85f)
            .padding(vertical = 4.dp)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            shape = RoundedCornerShape(20.dp),
            modifier = modifierField,
            isError = !Validator.isNameValid(name) && name.isNotEmpty()
        )
        if (!Validator.isNameValid(name) && name.isNotEmpty()) {
            Text(
                text = "El nombre no es válido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname ") },
            shape = RoundedCornerShape(20.dp),
            modifier = modifierField,
            isError = !Validator.isNameValid(surname) && surname.isNotEmpty()
        )
        if (!Validator.isNameValid(surname) && surname.isNotEmpty()) {
            Text(
                text = "El apellido no es válido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            placeholder = { Text("example@example.com") },
            isError = email.isNotEmpty() && !isEmailValid,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(20.dp)
        )
        if (!isEmailValid && email.isNotEmpty()) {
            Text(
                text = "El correo no es válido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(20.dp),
            modifier = modifierField,
            isError = !Validator.isPhoneNumberValid(phone) && phone.isNotEmpty()
        )
        if (!Validator.isPhoneNumberValid(phone) && phone.isNotEmpty()) {
            Text(
                text = "El número de teléfono no es válido",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Date Of Birth") },
            placeholder = { Text("DD / MM / YYYY") },
            isError = !Validator.isDateValid(birthDate) && birthDate.isNotEmpty(),
            shape = RoundedCornerShape(20.dp),
            modifier = modifierField
        )
        if (!Validator.isDateValid(birthDate) && birthDate.isNotEmpty()) {
            Text(
                text = "La fecha no es válida",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_eye_close),
                        contentDescription = "Mostrar/Ocultar"
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = modifierField,
            isError = !Validator.isPasswordValid(password) && password.isNotEmpty()
        )
        if (!Validator.isPasswordValid(password) && password.isNotEmpty()) {
            Text(
                text = "La contraseña no es válida",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_eye_close),
                        contentDescription = "Mostrar/Ocultar"
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = modifierField,
            isError = !Validator.isPasswordValid(confirmPassword)
                    && confirmPassword.isNotEmpty()
                    && !Validator.doPasswordsMatch(password, confirmPassword)
        )
        if (!Validator.doPasswordsMatch(password, confirmPassword)
            && confirmPassword.isNotEmpty()
        ) {
            Text(
                text = "Las contraseñas no coinciden",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Al continuar, tu estás de acuerdo con los",
            style = MaterialTheme.typography.bodySmall
        )

        Row {
            Text(
                text = "Términos de uso ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "y Política de privacidad.",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showSuccessDialog = true
                showConfetti = true
            },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            enabled = Validator.isFormValid(
                name,
                surname,
                email,
                phone,
                birthDate,
                password,
                Validator.doPasswordsMatch(password, confirmPassword),
            ) && isEmailValid
        ) {
            Text("Regístrate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Ya tienes una cuenta?")
            TextButton(onClick = onLoginNavigate) {
                Text("Inicia sesión")
            }
        }
    }

    if (showConfetti) {
        ConfettiAnimation(
            onAnimationEnd = {
                showConfetti = false
            },
        )
    }

    if (showSuccessDialog && !showConfetti) {
        SuccessDialog(
            message = "¡Registro exitoso!",
            onDismiss = {
                showSuccessDialog = false;
                onRegisterClick()
            }
        )
    }

}
