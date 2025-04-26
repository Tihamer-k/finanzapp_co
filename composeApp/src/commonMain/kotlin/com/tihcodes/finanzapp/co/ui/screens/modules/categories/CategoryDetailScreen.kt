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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.getSampleCategories
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_category
import finanzapp_co.composeapp.generated.resources.ic_food
import finanzapp_co.composeapp.generated.resources.ic_gifts
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategoryDetailScreen(
    categoryName: String,
    navController: NavHostController
) {
    val categoryItem = getSampleCategories().find { it.name == categoryName }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = categoryName,
                notificationsCount = 0,
                showBackButton = true // Nuevo parámetro para mostrar el botón de back
            )
        },
        content = { paddingValues ->
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
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    categoryItem?.let { category ->
                        if (category.nameReference == "More") {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Agregar nueva categoría",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                NewCategoryForm(
                                    onCreate = { name, icon, color ->
                                        println("INFO: Creada nueva categoría: $name con ícono $icon y color $color")
                                        navController.popBackStack()
                                    },
                                    onCancel = {
                                        println("INFO: Formulario cancelado")
                                        navController.popBackStack()
                                    }
                                )
                            }
                        } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Categoría: ${category.name}",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Inserta aquí el contenido dinámico que deseas mostrar
                            Text(
                                text = "Contenido de ${category.name}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        }
                    } ?: Text(
                        text = "Categoría no encontrada",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    )
}


private val iconOptions = listOf(
    Pair("regalos", Res.drawable.ic_gifts),
    Pair("Comida", Res.drawable.ic_food)
)
private val colorOptions = listOf(
    Pair("Rojo", Color.Red),
    Pair("Azul", Color.Blue),
    Pair("Verde", Color.Green)
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
            onExpandChange = { isIconMenuExpanded = it },
            onItemSelected = { selectedIcon = it }
        )

        // Selector de color
        ColorDropdownMenu(
            selectedColor = selectedColor,
            isExpanded = isColorMenuExpanded,
            onExpandChange = { isColorMenuExpanded = it },
            onItemSelected = { selectedColor = it }
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
                            Text(name,
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

