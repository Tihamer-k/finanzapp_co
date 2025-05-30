package com.tihcodes.finanzapp.co.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.utils.Validator
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_calendar
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    var showDatePicker by rememberSaveable { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Editar Perfil",
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
                val modifierField = Modifier
                    .fillMaxWidth(0.87f)
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(20.dp))

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
                        "${uiState.name.firstOrNull() ?: ""}${uiState.surname.firstOrNull() ?: ""}".uppercase()
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 32.sp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nombre") },
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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.surname,
                    onValueChange = viewModel::onSurnameChange,
                    label = { Text("Apellido") },
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

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = viewModel::onPhoneChange,
                    label = { Text("Teléfono") },
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

                Spacer(modifier = Modifier.height(8.dp))

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
                    startDate = LocalDate.now().minus(DatePeriod(years = 18)),
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


                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.onUpdateUserData()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(width = 300.dp, height = 48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = Validator.isNameValid(uiState.name) && uiState.name.isNotEmpty() &&
                            Validator.isNameValid(uiState.surname) && uiState.surname.isNotEmpty() &&
                            Validator.isPhoneNumberValid(uiState.phone) && uiState.phone.isNotEmpty() &&
                            Validator.isDateValid(uiState.date) && uiState.date.isNotEmpty()
                ) {
                    Text(
                        "Guardar Cambios",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }
    }
}

