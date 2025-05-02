package com.tihcodes.finanzapp.co.ui.screens.modules.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.CategoryItem
import com.tihcodes.finanzapp.co.data.repository.CategoryRepository
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import finanzapp_co.composeapp.generated.resources.*
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
                notificationsCount = 0,
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

private val iconOptions = listOf(
    Pair("Regalos", Res.drawable.ic_gifts),
    Pair("Comida", Res.drawable.ic_food),
    Pair("Transporte", Res.drawable.ic_transport),
    Pair("Bebés", Res.drawable.ic_baby),
    Pair("Trabajo", Res.drawable.ic_work),
    Pair("Viajes", Res.drawable.ic_travel),
    Pair("Entretenimiento", Res.drawable.ic_entertainmentame),
    Pair("Monetario", Res.drawable.ic_moneysim),
    Pair("Medicina", Res.drawable.ic_medicine),
    Pair("Hogar", Res.drawable.ic_home_expenses),
    Pair("Despensa", Res.drawable.ic_groceries),
    Pair("Ahorros", Res.drawable.ic_savings_pig),
    Pair("Ingresos", Res.drawable.ic_savings)
)

private val colorOptions = listOf(
    Pair("Rojo", Color(0xFFFF0000)),
    Pair("Azul", Color(0xFF0000FF)),
    Pair("Verde", Color(0xFF00FF00)),
    Pair("Amarillo", Color(0xFFFFFF00)),
    Pair("Naranja", Color(0xFFFFA500)),
    Pair("Morado", Color(0xFF800080)),
    Pair("Rosa", Color(0xFFFFC0CB)),
    Pair("Cyan", Color(0xFF00FFFF)),
    Pair("Gris", Color(0xFF808080)),
    Pair("Negro", Color(0xFF000000)),
    Pair("Blanco", Color(0xFFFFFFFF)),
    Pair("Marrón", Color(0xFF8B4513)),
    Pair("Turquesa", Color(0xFF40E0D0)),
    Pair("Lima", Color(0xFF32CD32)),
    Pair("Oro", Color(0xFFFFD700)),
    Pair("Plata", Color(0xFFC0C0C0)),
    Pair("Lavanda", Color(0xFFE6E6FA)),
    Pair("Durazno", Color(0xFFFFE5B4)),
    Pair("Aguamarina", Color(0xFF7FFFD4)),
    Pair("Coral", Color(0xFFFF7F50))
)

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
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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
                                    .size(30.dp)
                                    .padding(end = 8.dp)
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
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 16.dp)
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
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
