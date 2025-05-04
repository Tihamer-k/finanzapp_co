package com.tihcodes.finanzapp.co.presentation.screen.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel

@Composable
fun QuestionsScreen(
    courseId: String,
    viewModel: AuthViewModel,
    navController: NavController
) {
    val questions by viewModel.questions.collectAsState()
    val courseQuestions = questions[courseId] ?: emptyList()
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var isCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isCompleted) {
            Text(
                text = courseQuestions[currentQuestionIndex],
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (currentQuestionIndex < courseQuestions.size - 1) {
                    currentQuestionIndex++
                } else {
                    isCompleted = true
                    viewModel.completeQuestions(courseId)
                    navController.popBackStack()
                }
            }) {
                Text("Siguiente")
            }
        } else {
            Text(
                text = "¡Has completado las preguntas!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
