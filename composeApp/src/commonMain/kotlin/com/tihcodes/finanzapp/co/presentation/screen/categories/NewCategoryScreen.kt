package com.tihcodes.finanzapp.co.presentation.screen.categories

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.CategoryItem
import com.tihcodes.finanzapp.co.domain.model.NotificationItem
import com.tihcodes.finanzapp.co.domain.repository.CategoryRepository
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.viewmodel.AppNotificationViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.NotificationViewModel
import com.tihcodes.finanzapp.co.utils.Validator.randomId
import com.tihcodes.finanzapp.co.utils.colorOptions
import com.tihcodes.finanzapp.co.utils.getColorIdentifier
import com.tihcodes.finanzapp.co.utils.iconOptions
import compose.icons.TablerIcons
import compose.icons.tablericons.Confetti
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

@Composable
fun NewCategoryScreen(
    navController: NavHostController
) {
    val categoryRepository = koinInject<CategoryRepository>()
    val authViewModel = koinInject<AuthViewModel>()

    // Get current user ID from AuthViewModel
    val currentUser = authViewModel.currentUser.collectAsState().value
    val userId = currentUser?.id ?: ""

    // State for loading indicator
    var isLoading by remember { mutableStateOf(false) }

    val notificationViewModel = koinInject<NotificationViewModel>()
    val appNotificationViewModel = koinInject<AppNotificationViewModel>()

    // Sync categories with Firestore when the screen is loaded
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            categoryRepository.syncCategories(userId)
        }
    }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Nueva Categoría",
                showBackButton = true
            )
        }
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
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Agregar nueva categoría",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    NewCategoryForm(
                        onCreate = { name: String, icon: DrawableResource, color: Color ->
                            isLoading = true
                            // Create a new CategoryItem and add it to the repository
                            val newCategory = CategoryItem(
                                name = name,
                                icon = icon,
                                backgroundColor = color,
                                nameReference = name,
                                userId = userId
                            )

                            // Add category to repository (which will save to Firestore)
                            categoryRepository.addCategory(newCategory)

                            // Log success and navigate back
                            println("INFO: Created new category: $name with icon $icon and color $color")
                            isLoading = false
                            notificationViewModel.executeNotification(
                                title = "Categoría creada",
                                description = "La categoría '$name' ha sido creada exitosamente.",
                            )
                            appNotificationViewModel.setNotification(
                                notification = NotificationItem(
                                    id = randomId(),
                                    icon = TablerIcons.Confetti.name,
                                    title = "Categoría creada",
                                    message = "La categoría '$name' ha sido creada exitosamente.",
                                    dateTime = LocalDate.now().toString(),
                                    categoryTag = name,
                                    categoryColor = getColorIdentifier(Color(0xFFFFA500)),
                                    isRead = false,
                                    userId = userId
                                )
                            )
                            navController.popBackStack()
                        },
                        onCancel = {
                            println("INFO: Form cancelled")
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NewCategoryForm(
    onCreate: (String, DrawableResource, Color) -> Unit,
    onCancel: () -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<DrawableResource?>(null) }
    var isIconMenuExpanded by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Color.Transparent) }
    var isColorMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(24.dp)
    ) {
        // Nombre de la categoría
        TextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("Nombre de la categoría") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Selector de ícono
        IconDropdownMenu(
            selectedIcon = selectedIcon,
            isExpanded = isIconMenuExpanded,
            onExpandChange = { expanded -> isIconMenuExpanded = expanded },
            onItemSelected = { icon -> selectedIcon = icon }
        )

        // Selector de color
        ColorDropdownMenu(
            selectedColor = selectedColor,
            isExpanded = isColorMenuExpanded,
            onExpandChange = { expanded -> isColorMenuExpanded = expanded },
            onItemSelected = { color -> selectedColor = color }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botones
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    selectedIcon?.let { icon ->
                        onCreate(categoryName, icon, selectedColor)
                    }
                },
                enabled = categoryName.isNotBlank() && selectedIcon != null && selectedColor != Color.Transparent
            ) {
                Text("Agregar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconDropdownMenu(
    selectedIcon: DrawableResource?,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onItemSelected: (DrawableResource) -> Unit
) {
    val selectedName = iconOptions.find { it.second == selectedIcon }?.first ?: "Seleccionar Icono"

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandChange
    ) {
        TextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Ícono") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.primary
            )
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
            expanded = isExpanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            iconOptions.forEach { (name, icon) ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(icon),
                                contentDescription = name,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 16.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            )
                            Text(
                                name,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        onItemSelected(icon)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDropdownMenu(
    selectedColor: Color,
    isExpanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onItemSelected: (Color) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandChange
    ) {
        TextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Color") },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (selectedColor != Color.Transparent) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant)
                )
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            colorOptions.forEach { (name, color) ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(60.dp))
                                    .background(color)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    },
                    onClick = {
                        onItemSelected(color)
                        onExpandChange(false)
                    }
                )
            }
        }
    }
}
