package com.tihcodes.finanzapp.co.ui.screens.modules.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.CategoryItem
import com.tihcodes.finanzapp.co.data.repository.CategoryRepository
import com.tihcodes.finanzapp.co.ui.BottomNavBar
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.components.BalanceSummary
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import finanzapp_co.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject


@Composable
fun CategoryScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {
    val categoryRepository = koinInject<CategoryRepository>()
    val gridState = rememberLazyGridState()

    // Get current user ID
    val userId = viewModel.currentUser.collectAsState().value?.id ?: ""

    // Initialize repository if needed with user ID and sync with Firestore
    LaunchedEffect(userId) {
        // First initialize with default categories
        categoryRepository.initialize(userId)

        // Then sync with Firestore to get user-specific categories
        if (userId.isNotEmpty()) {
            categoryRepository.syncCategories(userId)
        }
    }

    // Get categories from repository filtered by user ID
    val categories = remember(userId) { categoryRepository.getAllCategories(userId) }

    // Detect if we should show the FAB
    val isFabVisible by remember {
        derivedStateOf {
            gridState.firstVisibleItemScrollOffset == 0 ||
                    gridState.isScrollInProgress.not()
        }
    }

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
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFabVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { 
                        // Navigate to new category screen
                        navController.navigate("new_category")
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_more),
                        contentDescription = "Agregar categoría",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
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

                BalanceSummary()

                Spacer(modifier = Modifier.height(36.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    state = gridState,
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp) // Add padding at the bottom for better scrolling
                ) {
                    items(
                        items = categories,
                        key = { it.name + it.userId } // Use a unique key for better performance
                    ) { category ->
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
        CategoryItem("Food", Res.drawable.ic_food, Color(0xFF0000FF), "Food"), // blue
        CategoryItem("Transport", Res.drawable.ic_transport, Color(0xFF00FFFF), "Transport"), // cyan
        CategoryItem("Medicine", Res.drawable.ic_medicine, Color(0xFF00FF00), "Medicine"), // green
        CategoryItem("Groceries", Res.drawable.ic_groceries, Color(0xFFFFA500), "Groceries"), // orange
        CategoryItem("Rent", Res.drawable.ic_home_expenses, Color(0xFFFF0000), "Rent"), // red
        CategoryItem("Gifts", Res.drawable.ic_gifts, Color(0xFF800080), "Gifts"), // purple
        CategoryItem("Savings", Res.drawable.ic_savings, Color(0xFFFFD700), "Savings"), // gold
        CategoryItem(
            "Entertainment",
            Res.drawable.ic_entertainmentame,
            Color(0xFF40E0D0), // turquoise
            "Entertainment"
        )
    )
}
