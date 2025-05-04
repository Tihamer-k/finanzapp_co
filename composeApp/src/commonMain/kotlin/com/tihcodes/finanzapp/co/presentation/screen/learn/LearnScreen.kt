package com.tihcodes.finanzapp.co.presentation.screen.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.domain.model.User
import com.tihcodes.finanzapp.co.presentation.components.BottomNavBar
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel

@Composable
fun LearnScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {

    val user = viewModel.currentUser.collectAsState().value ?: User()
    val courses by viewModel.courses.collectAsState()
    val progress by viewModel.progress.collectAsState()

    LaunchedEffect(Unit) {
        if (courses.isEmpty()) {
            viewModel.loadCourses()
        }
    }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Aprende",
                notificationsCount = 0,
                showBackButton = false,
            )
        },
        bottomBar = {
            BottomNavBar(
                indexIn = 1,
                onItemClick = navController
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
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {

                Text(
                    text = "Módulo de Aprendizaje Financiero",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progreso global
                Text(
                    text = "Avance: ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(courses) { course ->
                        CourseItem(
                            course = course,
                            onClick = {
                                if (course.isUnlocked && !course.isCompleted) {
                                    if (course.hasPendingQuestions) {
                                        navController.navigate("course/${course.id}/isCompleted=${false}")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

    }
}
