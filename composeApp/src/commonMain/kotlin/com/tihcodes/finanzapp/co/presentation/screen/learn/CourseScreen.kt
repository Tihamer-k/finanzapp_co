package com.tihcodes.finanzapp.co.presentation.screen.learn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.presentation.components.BottomNavBar
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.CourseTrackingViewModel
import com.tihcodes.finanzapp.co.presentation.viewmodel.NotificationViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.onboarding_2_business_plan_amico
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CourseContentScreen(
    courseId: String,
    viewModel: AuthViewModel,
    navController: NavController,
    isCompleted: Boolean
) {
    val courseTrackingViewModel = koinViewModel<CourseTrackingViewModel>()
    val courses by courseTrackingViewModel.courses.collectAsState()
    val course = courses.find { it.id == courseId } ?: return

    var showQuestions by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var answerFeedback by remember { mutableStateOf<String?>(null) }
    var correctAnswersCount by remember { mutableStateOf(0) }
    val user = viewModel.currentUser.collectAsState().value
    var isCompletedValue by remember { mutableStateOf(isCompleted) }
    val notificationViewModel = koinViewModel<NotificationViewModel>()


    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = course.title,
                notificationsCount = 0,
                showBackButton = true,
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
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(8.dp))
                Text(
                    course.description,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))

                if (!showQuestions) {
                    Image(
                        painter = painterResource(Res.drawable.onboarding_2_business_plan_amico),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(160.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            course.content
                                .replace("\\n", "\n")
                                .replace("\\t", "\t")
                                .replace("\\r", "\r"),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(18.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Justify
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                    if (!isCompletedValue) {
                        Button(
                            onClick = { navController.navigate("course-content/${course.id}") },
                        ) {
                            Text("Empezar Curso")
                        }
                    }
                    if (isCompletedValue) {
                        Button(onClick = { showQuestions = true }) {
                            Text("Empezar Cuestionario")
                        }
                    }
                } else {
                    val question = course.questions[currentQuestionIndex]

                    Text(
                        "Pregunta ${currentQuestionIndex + 1}/${course.questions.size}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(question.text, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))

                    question.options.forEach { option ->
                        Button(
                            onClick = {
                                selectedOption = option
                                answerFeedback = if (option == question.correctAnswer) {
                                    correctAnswersCount++
                                    "✅ ¡Correcto!"
                                } else {
                                    "❌ Incorrecto"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .padding(horizontal = 16.dp),
                            enabled = selectedOption == null
                        ) {
                            Text(option)
                        }
                    }

                    if (answerFeedback != null) {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            answerFeedback!!,
                            color = if (answerFeedback!!.contains("Correcto")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )

                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (currentQuestionIndex < course.questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedOption = null
                                    answerFeedback = null
                                } else {
                                    val passed = correctAnswersCount == course.questions.size
                                    if (passed) {
                                        course.hasPendingQuestions = false
                                        if (user != null) {
                                            courseTrackingViewModel.completeCourse(courseId, user.id, true)
                                            notificationViewModel.executeNotification(
                                                title = "Curso Completado",
                                                description = "¡Felicidades! Has completado el curso: ${course.title}, revisa tu recompensa.",
                                            )
                                        }
                                        navController.navigate("learn") {
                                            popUpTo("learn") { inclusive = true }
                                        }
                                    } else {
                                        navController.popBackStack()
                                        isCompletedValue = false
                                    }
                                }
                            }
                        ) {
                            Text(if (currentQuestionIndex < course.questions.size - 1) "Siguiente" else "Finalizar")
                        }
                    }
                }
            }
        }
    }
}
