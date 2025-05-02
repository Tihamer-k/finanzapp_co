package com.tihcodes.finanzapp.co.ui.screens.modules.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.data.CategoryItem
import com.tihcodes.finanzapp.co.data.repository.CategoryRepository
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.components.ExpandableFab
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import org.koin.compose.koinInject


@Composable
fun CategoryDetailScreen(
    categoryName: String,
    navController: NavHostController
) {
    val categoryRepository = koinInject<CategoryRepository>()
    val authViewModel = koinInject<AuthViewModel>()

    // Get current user ID from AuthViewModel
    val currentUser = authViewModel.currentUser.collectAsState().value
    val userId = currentUser?.id ?: ""

    // Initialize repository if needed with user ID and sync with Firestore
    LaunchedEffect(userId) {
        // First initialize with default categories
        categoryRepository.initialize(userId)

        // Then sync with Firestore to get user-specific categories
        if (userId.isNotEmpty()) {
            categoryRepository.syncCategories(userId)
        }
    }

    // Get category from repository or fallback to sample categories
    val categoryItem = remember(userId) {
        categoryRepository.getCategoryByName(categoryName, userId)
            ?: getSampleCategories().find { it.name == categoryName }?.copy(userId = userId)
    }

    // Show/hide FAB based on whether we're in the "More" category
    val showFab = categoryItem != null && categoryItem.nameReference != "More"

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = categoryName,
                notificationsCount = 0,
                showBackButton = true // Nuevo parámetro para mostrar el botón de back
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showFab,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ExpandableFab(
                    onAddIncome = {
                        // Navigate to add income with pre-selected category
                        navController.navigate("new_transaction_income?category=$categoryName")
                    },
                    onAddExpense = {
                        // Navigate to add expense with pre-selected category
                        navController.navigate("new_transaction_expense?category=$categoryName")
                    }
                )
            }
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
                                        // Create a new CategoryItem and add it to the repository
                                        val newCategory = CategoryItem(
                                            name = name,
                                            icon = icon,
                                            backgroundColor = color,
                                            nameReference = name,
                                            userId = userId
                                        )
                                        categoryRepository.addCategory(newCategory)
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
                    }
                }
            }
        }
    )
}


// Form components moved to NewCategoryScreen.kt
