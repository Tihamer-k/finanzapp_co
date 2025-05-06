package com.tihcodes.finanzapp.co.presentation.screen.records

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.CategoryItem
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.domain.repository.TransactionRepository
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_budget
import finanzapp_co.composeapp.generated.resources.ic_calendar
import finanzapp_co.composeapp.generated.resources.ic_expense
import finanzapp_co.composeapp.generated.resources.ic_income
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTransactionScreen(
    navController: NavHostController,
    type: TransactionType,
    transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
    userId: String,
    preSelectedCategory: String = ""
) {
    // State for form fields
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<CategoryItem?>(null) }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedDate by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }

    // Use a default userId if the provided userId is empty
    // This is a workaround for the issue where the userId is not being correctly passed to the NewTransactionScreen
    val effectiveUserId = remember(userId) {
        userId.ifEmpty {
            println("[DEBUG_LOG] Using default userId because provided userId is empty")
            "default_user_id"  // Use a default userId if the provided userId is empty
        }
    }

    // Log the userId parameter for debugging
    println("[DEBUG_LOG] NewTransactionScreen received userId: $userId")
    println("[DEBUG_LOG] Using effectiveUserId: $effectiveUserId")

    // Initialize repository with effective user ID
    LaunchedEffect(effectiveUserId) {
        println("[DEBUG_LOG] LaunchedEffect triggered with effectiveUserId: $effectiveUserId")

        // First initialize with default categories
        println("[DEBUG_LOG] Initializing category repository")
        categoryRepository.initialize(effectiveUserId)

        // Then sync with Firestore to get user-specific categories
        println("[DEBUG_LOG] Starting category sync")
        try {
            categoryRepository.syncCategories(effectiveUserId)
            println("[DEBUG_LOG] Category sync completed")
        } catch (e: Exception) {
            println("[DEBUG_LOG] Error during category sync: ${e.message}")
            e.printStackTrace()
        }

        // Force refresh of categories after sync
        println("[DEBUG_LOG] Categories after sync: ${categoryRepository.categories.value.size}")
    }

    // Add a second LaunchedEffect to ensure UI updates after categories are loaded
    LaunchedEffect(Unit) {
        println("[DEBUG_LOG] Secondary LaunchedEffect to ensure categories are loaded")
        // This empty effect ensures the Compose runtime re-evaluates the UI after the first LaunchedEffect completes
    }

    // Get categories from repository
    val categories by categoryRepository.categories.collectAsState()
    val userCategories = categories.filter { it.userId == effectiveUserId || it.userId.isEmpty() }

    // Debug the categories
    LaunchedEffect(categories) {
        println("[DEBUG_LOG] Found ${categories.size} total categories")
        println("[DEBUG_LOG] Found ${userCategories.size} categories for user $effectiveUserId")
        println("[DEBUG_LOG] Categories state: ${if (categories.isEmpty()) "empty" else "not empty"}")
        println("[DEBUG_LOG] UserCategories state: ${if (userCategories.isEmpty()) "empty" else "not empty"}")

        userCategories.forEach {
            println("[DEBUG_LOG] Category: ${it.name}, userId: ${it.userId}")
        }

        // Pre-select category if specified
        if (preSelectedCategory.isNotEmpty()) {
            val category = userCategories.find { it.name == preSelectedCategory }
            if (category != null) {
                selectedCategory = category
                println("[DEBUG_LOG] Pre-selected category: ${category.name}")
            } else {
                println("[DEBUG_LOG] Could not find pre-selected category: $preSelectedCategory")
            }
        }
    }

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }

    // Get icon based on transaction type
    val icon = when (type) {
        TransactionType.INCOME -> Res.drawable.ic_income
        TransactionType.EXPENSE -> Res.drawable.ic_expense
        TransactionType.BUDGET -> Res.drawable.ic_budget
    }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = type.name,
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Agregar nueva transacción",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // Title field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && title.isEmpty()
                )

                // Amount field
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        // Only allow numbers and decimal point
                        if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                        }
                    },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isError && (amount.isEmpty() || amount.toDoubleOrNull() == null)
                )

                // Campo de fecha con ícono de calendario
                OutlinedTextField(
                    value = selectedDate.toString(),
                    onValueChange = {},
                    label = { Text("Fecha") },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_calendar),
                            contentDescription = "Seleccionar fecha",
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp).size(24.dp)
                                .clickable {
                                    println("[DEBUG_LOG] Calendar icon clicked")
                                    showDatePicker = true
                                },
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,

                            )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                WheelDatePickerView(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    dateTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    showDatePicker = showDatePicker,
                    title = "Seleccione una fecha",
                    doneLabel = "Aceptar",
                    titleStyle = MaterialTheme.typography.labelMedium,
                    doneLabelStyle = MaterialTheme.typography.labelMedium,
                    startDate = LocalDate.now(),
                    selectorProperties = WheelPickerDefaults. selectorProperties(
                        borderColor = MaterialTheme.colorScheme.primary.copy(0.7f),
                    ),
                    height = 200.dp,
                    dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
                    rowCount = 3,
                    onDoneClick = {
                        println("[DEBUG_LOG] Date selected: $it")
                        selectedDate = it
                        showDatePicker = false
                    },
                    onDismiss = {
                        println("[DEBUG_LOG] Date picker dismissed")
                        showDatePicker = false
                    },
                    yearsRange = IntRange(1920, LocalDate.now().year),
                )

                // Category dropdown
                println("[DEBUG_LOG] Rendering dropdown with ${userCategories.size} categories")
                println("[DEBUG_LOG] Dropdown expanded state: $expanded")

                // Create a local copy of the categories to ensure they're available in the dropdown
                val categoriesForDropdown = remember(userCategories) {
                    println("[DEBUG_LOG] Remembering ${userCategories.size} categories for dropdown")
                    userCategories.toList()
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Add a debug text to show how many categories are available
                    if (categoriesForDropdown.isEmpty()) {
                        Text(
                            text = "No hay categorías disponibles. Por favor espere...",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    } else {
                        Text(
                            text = "${categoriesForDropdown.size} categorías disponibles",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it
                            println("[DEBUG_LOG] Dropdown expanded changed to: $it")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategory?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            isError = isError && selectedCategory == null
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                                println("[DEBUG_LOG] Dropdown dismissed")
                            },
                            modifier = Modifier.exposedDropdownSize()
                        ) {
                            if (categoriesForDropdown.isEmpty()) {
                                println("[DEBUG_LOG] No categories to display in dropdown")
                                DropdownMenuItem(
                                    text = { Text("No hay categorías disponibles") },
                                    onClick = { expanded = false }
                                )
                            } else {
                                println("[DEBUG_LOG] Displaying ${categoriesForDropdown.size} categories in dropdown")
                                categoriesForDropdown.forEach { category ->
                                    println("[DEBUG_LOG] Adding category to dropdown: ${category.name}")
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                            println("[DEBUG_LOG] Selected category: ${category.name}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Error message
                if (isError && errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Save button
                LaunchedEffect(errorMessage) {
                    delay(4000) // 4 segundos
                    errorMessage = ""
                }
                Button(
                    onClick = {

                        // Validar campos
                        if (title.isEmpty() || amount.isEmpty() || selectedCategory == null) {
                            isError = true
                            errorMessage = "Por favor complete todos los campos"
                            return@Button
                        }

                        val amountValue = amount.toDoubleOrNull()
                        if (amountValue == null) {
                            isError = true
                            errorMessage = "El monto debe ser un número válido"
                            return@Button
                        }

                        // Validar fecha seleccionada
                        if (selectedDate > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date) {
                            isError = true
                            errorMessage = "La fecha no puede ser futura"
                            return@Button
                        }

                        // Crear transacción
                        val transaction = TransactionItem(
                            id = "",
                            title = title,
                            category = selectedCategory!!.name,
                            date = selectedDate,
                            amount = amountValue,
                            type = type,
                            icon = icon,
                            userId = effectiveUserId
                        )

                        // Guardar transacción
                        transactionRepository.addTransaction(transaction)

                        // Navegar hacia atrás
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Guardar")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

