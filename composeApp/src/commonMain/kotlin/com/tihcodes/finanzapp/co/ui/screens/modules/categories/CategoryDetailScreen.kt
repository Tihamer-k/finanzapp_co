import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.screens.modules.categories.getSampleCategories

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
            categoryItem?.let { category ->
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
            } ?: Text(
                text = "Categoría no encontrada",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}