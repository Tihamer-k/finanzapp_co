package com.tihcodes.finanzapp.co.presentation.screen.learn

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.domain.model.CoursesPage
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.course_image1
import finanzapp_co.composeapp.generated.resources.course_image2
import finanzapp_co.composeapp.generated.resources.course_image3
import finanzapp_co.composeapp.generated.resources.course_image4
import finanzapp_co.composeapp.generated.resources.course_image5
import finanzapp_co.composeapp.generated.resources.course_image6
import finanzapp_co.composeapp.generated.resources.course_image7
import finanzapp_co.composeapp.generated.resources.course_image8
import finanzapp_co.composeapp.generated.resources.onboarding_1_piggy_bank_amico
import finanzapp_co.composeapp.generated.resources.onboarding_2_business_plan_amico
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource


@Composable
fun CoursesModule(
    navController: NavController,
    courseId: String,
    viewModel: AuthViewModel
) {

    val drawableImages = listOf(
        Res.drawable.onboarding_1_piggy_bank_amico,
        Res.drawable.onboarding_2_business_plan_amico,
        Res.drawable.course_image1,
        Res.drawable.course_image2,
        Res.drawable.course_image3,
        Res.drawable.course_image4,
        Res.drawable.course_image5,
        Res.drawable.course_image6,
        Res.drawable.course_image7,
        Res.drawable.course_image8
    ).shuffled()

    var optionPages = when (courseId) {
        "1" -> pages1(drawableImages)
        "2" -> pages2(drawableImages)
        "3" -> pages3(drawableImages)
        "4" -> pages4(drawableImages)
        else -> pages1(drawableImages)
    }

    val pages = optionPages.toMutableList()


    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState
    ) { page ->
        if (page < pages.size - 1) {
            CoursePages(
                imageRes = pages[page].imageRes,
                title = pages[page].title,
                description = pages[page].description,
                onNextClick = {
                    if (page < pages.size) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page + 1)
                        }
                    }
                },
                currentPage = page,
            )
        } else {
            CoursePages(
                imageRes = pages[page].imageRes,
                title = pages[page].title,
                description = pages[page].description,
                onNextClick = {
                    navController.navigate(
                        "course/${courseId}/isCompleted=${true}"
                    ) {
                        popUpTo("course-content/${courseId}") {
                            inclusive = true
                        }
                    }
                },
                currentPage = page,
            )
        }
    }


}

fun pages4(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[9],
            title = "¿Qué es la educación financiera?",
            description = "La educación financiera te ayuda a entender cómo funciona el dinero.\\n Aprendes a administrar ingresos, ahorrar, evitar deudas innecesarias y tomar mejores decisiones económicas."
        ),
        CoursesPage(
            imageRes = drawableImages[5],
            title = "¿Por qué es importante para los jóvenes?",
            description = "Muchos jóvenes no saben cómo manejar su dinero. Sin educación financiera, es fácil endeudarse o gastar más de lo que se tiene. Aprender desde ahora te da ventaja para el futuro."
        ),
        CoursesPage(
            imageRes = drawableImages[4],
            title = " Conceptos básicos que debes conocer",
            description = "Empieza por lo esencial:\u2028 • Presupuesto\u2028 • Ahorro\u2028 • Ingreso vs. Gasto\u2028 • Interés compuesto\u2028 Son la base de tu salud financiera."
        )
    )
}

fun pages3(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[6],
            title = "¿Qué es la educación financiera?",
            description = "La educación financiera te ayuda a entender cómo funciona el dinero.\\n Aprendes a administrar ingresos, ahorrar, evitar deudas innecesarias y tomar mejores decisiones económicas."
        ),
        CoursesPage(
            imageRes = drawableImages[7],
            title = "¿Por qué es importante para los jóvenes?",
            description = "Muchos jóvenes no saben cómo manejar su dinero. Sin educación financiera, es fácil endeudarse o gastar más de lo que se tiene. Aprender desde ahora te da ventaja para el futuro."
        ),
        CoursesPage(
            imageRes = drawableImages[8],
            title = " Conceptos básicos que debes conocer",
            description = "Empieza por lo esencial:\u2028 • Presupuesto\u2028 • Ahorro\u2028 • Ingreso vs. Gasto\u2028 • Interés compuesto\u2028 Son la base de tu salud financiera."
        ),
    )
}

fun pages2(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[3],
            title = "¿Qué es la educación financiera?",
            description = "La educación financiera te ayuda a entender cómo funciona el dinero.\\n Aprendes a administrar ingresos, ahorrar, evitar deudas innecesarias y tomar mejores decisiones económicas."
        ),
        CoursesPage(
            imageRes = drawableImages[4],
            title = "¿Por qué es importante para los jóvenes?",
            description = "Muchos jóvenes no saben cómo manejar su dinero. Sin educación financiera, es fácil endeudarse o gastar más de lo que se tiene. Aprender desde ahora te da ventaja para el futuro."
        ),
        CoursesPage(
            imageRes = drawableImages[5],
            title = " Conceptos básicos que debes conocer",
            description = "Empieza por lo esencial:\u2028 • Presupuesto\u2028 • Ahorro\u2028 • Ingreso vs. Gasto\u2028 • Interés compuesto\u2028 Son la base de tu salud financiera."
        ),
    )
}

fun pages1(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[0],
            title = "¿Qué es la educación financiera?",
            description = "La educación financiera te ayuda a entender cómo funciona el dinero.\\n Aprendes a administrar ingresos, ahorrar, evitar deudas innecesarias y tomar mejores decisiones económicas."
        ),
        CoursesPage(
            imageRes = drawableImages[1],
            title = "¿Por qué es importante para los jóvenes?",
            description = "Muchos jóvenes no saben cómo manejar su dinero. Sin educación financiera, es fácil endeudarse o gastar más de lo que se tiene. Aprender desde ahora te da ventaja para el futuro."
        ),
        CoursesPage(
            imageRes = drawableImages[2],
            title = " Conceptos básicos que debes conocer",
            description = "Empieza por lo esencial:\u2028 • Presupuesto\u2028 • Ahorro\u2028 • Ingreso vs. Gasto\u2028 • Interés compuesto\u2028 Son la base de tu salud financiera."
        ),
    )
}
