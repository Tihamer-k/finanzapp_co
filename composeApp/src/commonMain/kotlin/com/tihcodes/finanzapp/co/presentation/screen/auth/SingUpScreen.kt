package com.tihcodes.finanzapp.co.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.presentation.components.ConfettiAnimation
import com.tihcodes.finanzapp.co.presentation.components.SuccessDialog
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.utils.Validator
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_calendar
import finanzapp_co.composeapp.generated.resources.ic_eye_close
import finanzapp_co.composeapp.generated.resources.ic_eye_open
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource

@Composable
fun SignUpScreen(
    onRegisterClick: () -> Unit,
    onLoginNavigate: () -> Unit,
    viewModel: AuthViewModel
) {
    // Estados locales para inputs
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showConfetti by rememberSaveable { mutableStateOf(false) }

    var clicked by rememberSaveable { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val authState by viewModel.authState.collectAsState()
    var errorMessage by rememberSaveable { mutableStateOf("") }
    val isEmailValid = Validator.isEmailValid(uiState.email)
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var signInRes by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showConfetti) {
            ConfettiAnimation(
                onAnimationEnd = {
                    showConfetti = false
                },
            )
        }
        Spacer(
            modifier = Modifier.height(30.dp).fillMaxWidth()
        )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.height(30.dp))

            val modifierField = Modifier
                .fillMaxWidth(0.87f)
                .padding(vertical = 4.dp)
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(20.dp))

            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Name") },
                shape = RoundedCornerShape(20.dp),
                modifier = modifierField,
                isError = !Validator.isNameValid(uiState.name) && uiState.name.isNotEmpty()
            )
            if (!Validator.isNameValid(uiState.name) && uiState.name.isNotEmpty()) {
                Text(
                    text = "El nombre no es válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.surname,
                onValueChange = viewModel::onSurnameChange,
                label = { Text("Surname ") },
                shape = RoundedCornerShape(20.dp),
                modifier = modifierField,
                isError = !Validator.isNameValid(uiState.surname) && uiState.surname.isNotEmpty()
            )
            if (!Validator.isNameValid(uiState.surname) && uiState.surname.isNotEmpty()) {
                Text(
                    text = "El apellido no es válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Correo") },
                placeholder = { Text("example@example.com") },
                isError = uiState.email.isNotEmpty() && !isEmailValid,
                modifier = modifierField,
                shape = RoundedCornerShape(20.dp)
            )
            if (!isEmailValid && uiState.email.isNotEmpty()) {
                Text(
                    text = "El correo no es válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                label = { Text("Mobile Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(20.dp),
                modifier = modifierField,
                isError = !Validator.isPhoneNumberValid(uiState.phone) && uiState.phone.isNotEmpty()
            )
            if (!Validator.isPhoneNumberValid(uiState.phone) && uiState.phone.isNotEmpty()) {
                Text(
                    text = "El número de teléfono no es válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.date,
                onValueChange = viewModel::onDateChange,
                label = { Text("Date Of Birth") },
                placeholder = { Text("YYYY - MM - DD") },
                isError = !Validator.isDateValid(uiState.date) && uiState.date.isNotEmpty(),
                shape = RoundedCornerShape(20.dp),
                modifier = modifierField,
                trailingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_calendar),
                        contentDescription = "Seleccionar fecha",
                        modifier = Modifier.background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ).padding(6.dp).size(24.dp)
                            .clickable {
                                println("[DEBUG_LOG] Calendar icon clicked")
                                showDatePicker = true
                            },
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            )

            if (!Validator.isDateValid(uiState.date) && uiState.date.isNotEmpty()) {
                Text(
                    text = "La fecha no es válida",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            WheelDatePickerView(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                dateTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                showShortMonths = true,
                showDatePicker = showDatePicker,
                title = "Seleccione una fecha",
                doneLabel = "Aceptar",
                titleStyle = MaterialTheme.typography.labelMedium,
                doneLabelStyle = MaterialTheme.typography.labelMedium,
                startDate = LocalDate.now(),
                selectorProperties = WheelPickerDefaults.selectorProperties(
                    borderColor = MaterialTheme.colorScheme.primary.copy(0.7f),
                ),
                height = 200.dp,
                dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
                rowCount = 3,
                onDoneClick = {
                    println("[DEBUG_LOG] Date selected: $it")
                    uiState.date = it.toString()
                    showDatePicker = false
                },
                onDismiss = {
                    println("[DEBUG_LOG] Date picker dismissed")
                    showDatePicker = false
                },
                yearsRange = IntRange(1920, LocalDate.now().year - 18),
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = if (!passwordVisible) painterResource(Res.drawable.ic_eye_close)
                            else painterResource(Res.drawable.ic_eye_open),
                            contentDescription = "Mostrar/Ocultar contraseña"
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = modifierField,
                isError = !Validator.isPasswordValid(uiState.password).first && uiState.password.isNotEmpty()
            )
            val (isValidPass, errorMessagePassword) = Validator.isPasswordValid(uiState.password)
            if (!isValidPass && uiState.password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                if (errorMessagePassword != null) {
                    Text(
                        text = errorMessagePassword,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                ),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = if (!confirmPasswordVisible) painterResource(Res.drawable.ic_eye_close)
                            else painterResource(Res.drawable.ic_eye_open),
                            contentDescription = "Mostrar/Ocultar contraseña"
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp),
                modifier = modifierField,
                isError = !Validator.isPasswordValid(confirmPassword).first
                        && confirmPassword.isNotEmpty()
                        && !Validator.doPasswordsMatch(uiState.password, confirmPassword)
            )
            if (!Validator.doPasswordsMatch(uiState.password, confirmPassword)
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
                    signInRes = viewModel.onRegister()
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(width = 200.dp, height = 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = Validator.isFormValid(
                    uiState.name,
                    uiState.surname,
                    uiState.email,
                    uiState.phone,
                    uiState.date,
                    uiState.password,
                    Validator.doPasswordsMatch(uiState.password, confirmPassword),
                ) && isEmailValid
            ) {
                Text(
                    "Regístrate",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }

            if (clicked) {
                if (isProcessing) {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (!signInRes) {
                    errorMessage = "Error en el registro"
                    if (errorMessage.isNotEmpty()) {
                        LaunchedEffect(errorMessage) {
                            delay(3000) // 3 segundos
                            errorMessage = ""
                        }
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    clicked = false
                } else if (signInRes) {
                    showConfetti = true
                    showSuccessDialog = true
                    clicked = false
                }
            }

            if (showSuccessDialog && !showConfetti) {
                SuccessDialog(
                    message = "¡Registro exitoso!",
                    onDismiss = {
                        showSuccessDialog = false
                        onRegisterClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                TextButton(onClick = onLoginNavigate) {
                    Text(
                        text = "Inicia sesión",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}
