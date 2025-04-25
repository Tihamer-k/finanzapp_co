package com.tihcodes.finanzapp.co.ui.screens.modules.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.CategoryItem
import com.tihcodes.finanzapp.co.ui.BottomNavBar
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_entertainmentame
import finanzapp_co.composeapp.generated.resources.ic_food
import finanzapp_co.composeapp.generated.resources.ic_gifts
import finanzapp_co.composeapp.generated.resources.ic_groceries
import finanzapp_co.composeapp.generated.resources.ic_home_expenses
import finanzapp_co.composeapp.generated.resources.ic_medicine
import finanzapp_co.composeapp.generated.resources.ic_more
import finanzapp_co.composeapp.generated.resources.ic_savings
import finanzapp_co.composeapp.generated.resources.ic_transport


@Composable
fun CategoryScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {
    val categories = remember { getSampleCategories() }
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Categorías",
                notificationsCount = 3,
                showBackButton = false,
            )
        },
        bottomBar = {
            BottomNavBar(
                indexIn = 3,
                onItemClick = navController
            )
        }
    ) { paddingValues ->
        // Respetar el espacio de la barra inferior y el TopBar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues) // Respetar el padding del Scaffold
        ) {
            // Este Column ocupará todo el espacio restante
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f) // Garantiza el crecimiento para ocupar el espacio restante
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sección del balance total
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Balance",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "$7,783.00",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xfff3194c)
                        )
                    }
                    // Sección de gastos totales
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Expense",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "-$1,187.40",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF007AFF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categories) { category ->
                        CategoryGridItem(
                            item = category,
                            onClick = {
                                // Navegar a la página de esa categoría
                                navController.navigate("categoryDetail/${category.name}")
                            }
                        )
                    }
                }
            }
        }
    }
}

fun getSampleCategories(): List<CategoryItem> {
    return listOf(
        CategoryItem("Food", Res.drawable.ic_food, Color(0xFF007AFF)),
        CategoryItem("Transport", Res.drawable.ic_transport, Color(0xFF5AC8FA)),
        CategoryItem("Medicine", Res.drawable.ic_medicine, Color(0xFF34C759)),
        CategoryItem("Groceries", Res.drawable.ic_groceries, Color(0xFFFF9500)),
        CategoryItem("Rent", Res.drawable.ic_home_expenses, Color(0xFFFF2D55)),
        CategoryItem("Gifts", Res.drawable.ic_gifts, Color(0xFFAF52DE)),
        CategoryItem("Savings", Res.drawable.ic_savings, Color(0xFFFFCC00)),
        CategoryItem("Entertainment", Res.drawable.ic_entertainmentame, Color(0xFF5856D6)),
        CategoryItem("More", Res.drawable.ic_more, Color(0xFF8E8E93))
    )
}
