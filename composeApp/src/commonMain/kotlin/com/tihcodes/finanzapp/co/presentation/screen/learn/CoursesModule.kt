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

    val optionPages = when (courseId) {
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
                total = pages.size
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
                total = pages.size,
            )
        }
    }


}

fun pages4(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[9],
            title = "El mapa de tu futuro financiero",
            description = "La planificación financiera es como crear un mapa para un viaje: estableces adónde quieres ir (metas), decides qué ruta tomarás (estrategias) y preparas lo necesario para el camino (recursos).\n" +
                    "Una buena planificación financiera te ayuda a:\n" +
                    "•\tTomar el control de tu situación financiera actual\n" +
                    "•\tPrepararte para gastos futuros previsibles e imprevistos\n" +
                    "•\tReducir estrés y preocupaciones sobre el dinero\n" +
                    "•\tAlcanzar tus sueños y metas de vida\n"
        ),
        CoursesPage(
            imageRes = drawableImages[5],
            title = "Estableciendo metas financieras",
            description = "Las metas financieras deben ser SMART: Específicas, Medibles, Alcanzables, Relevantes y con Tiempo definido. Por ejemplo, \"Ahorrar \$300 en 6 meses para comprar un teléfono nuevo\" es mejor que \"Ahorrar para un teléfono\".\n" +
                    "Organiza tus metas por plazos:\n" +
                    "• Corto plazo (menos de 1 año): Crear un fondo de emergencia, comprar ropa para la escuela.\n" +
                    "• Mediano plazo (1-5 años): Comprar una computadora, pagar un viaje, tomar un curso.\n" +
                    "• Largo plazo (más de 5 años): Estudios universitarios, comprar una casa o un auto.\n"
        ),
        CoursesPage(
            imageRes = drawableImages[4],
            title = "Un plan financiero básico tiene estos componentes:",
            description = "• Presupuesto actualizado: Revisa y ajusta tu presupuesto regularmente para asegurarte de que sigue reflejando tu situación actual.\n" +
                    "• Fondo de emergencia: Antes de pensar en otras metas, asegura tener ahorros para imprevistos (3-6 meses de gastos básicos).\n" +
                    "• Plan de ahorro: Define cuánto necesitas ahorrar mensualmente para cada meta y automatiza estos ahorros si es posible.\n" +
                    "• Protección: Considera seguros básicos según tu situación (seguro médico, de vida, para tus dispositivos).\n" +
                    "• Revisiones periódicas: Evalúa tu progreso cada 3-6 meses y ajusta tu plan según sea necesario.\n"
        )
    )
}

fun pages3(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[6],
            title = "De ahorro a inversión",
            description = "Mientras que el ahorro te ayuda a conservar tu dinero, la inversión te ayuda a multiplicarlo. Invertir significa poner tu dinero a trabajar para que genere más dinero con el tiempo.\n" +
                    "La diferencia principal: cuando inviertes, aceptas cierto nivel de riesgo (la posibilidad de perder dinero) a cambio de la oportunidad de obtener mayores ganancias que simplemente ahorrando.\n"
        ),
        CoursesPage(
            imageRes = drawableImages[7],
            title = "Conceptos básicos de inversión",
            description = "• Rendimiento: Es la ganancia que obtienes de tu inversión, generalmente expresada como un porcentaje. Por ejemplo, un rendimiento del 5% anual significa que por cada \$100 invertidos, ganarías \$5 al año.\n" +
                    "• Riesgo: La posibilidad de perder parte o todo tu dinero. Generalmente, mayor potencial de ganancia implica mayor riesgo.\n" +
                    "• Interés compuesto: Es cuando ganas interés no solo sobre tu inversión inicial, sino también sobre los intereses acumulados anteriormente. Es como una bola de nieve que crece cada vez más rápido mientras rueda colina abajo.\n"
        ),
        CoursesPage(
            imageRes = drawableImages[8],
            title = "Tipos de inversiones para principiantes",
            description = "• Acciones: Representan una pequeña parte de propiedad en una empresa. Si a la empresa le va bien, el valor de tus acciones sube. Si le va mal, baja. Son más riesgosas pero pueden ofrecer mayores rendimientos.\n" +
                    "• Bonos: Son como préstamos que haces a empresas o gobiernos. Te pagan intereses durante un tiempo y luego te devuelven tu dinero. Son generalmente menos riesgosos que las acciones.\n" +
                    "• Fondos: Reúnen dinero de muchos inversores para invertir en múltiples acciones o bonos. Te permiten diversificar (no poner todos los huevos en la misma canasta) incluso con poco dinero.\n" +
                    "Recuerda: Nunca inviertas dinero que necesitarás pronto o que no puedes permitirte perder.\n"
        ),
    )
}

fun pages2(drawableImages: List<DrawableResource>): List<CoursesPage> {
    return listOf(
        CoursesPage(
            imageRes = drawableImages[3],
            title = "El poder del ahorro",
            description = "Ahorrar no significa guardar lo que te sobra después de gastar. El ahorro inteligente consiste en apartar dinero intencionalmente antes de empezar a gastar. Es como servirse primero un plato de comida para guardar antes de empezar a comer, en lugar de guardar las sobras."
        ),
        CoursesPage(
            imageRes = drawableImages[4],
            title = "Estrategias efectivas de ahorro",
            description = "•\t La regla 50/30/20: Divide tu dinero así:\n" +
                    "50% para necesidades (comida, vivienda)\n" +
                    "30% para deseos (salidas, entretenimiento)\n" +
                    "20% para ahorros (fondo de emergencia, metas)\n",
        ),
        CoursesPage(
            imageRes = drawableImages[5],
            title = "Metas de ahorro",
            description = "Tener metas claras hace que ahorrar sea más motivador. Clasifica tus metas así:\n" +
                    "•\t Corto plazo: cosas que quieres comprar pronto (ropa, videojuegos).\n" +
                    "•\t Mediano plazo: metas a 1-3 años (viajes, estudios).\n" +
                    "•\t Largo plazo: metas a más de 3 años\n" +
                    "(universidad, viajes importantes, tu primer auto).\n" +
                    "Asigna un valor y una fecha a cada meta para hacerla más concreta y alcanzable.\n",
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
            title = "¿Qué son las finanzas personales?",
            description = "Las finanzas personales se refieren a cómo manejas tu dinero día a día. Esto incluye todo lo relacionado con tu dinero: lo que ganas, lo que gastas, lo que ahorras y cómo lo haces crecer. Piensa en tus finanzas como un jardín que necesita cuidado constante para florecer."

        ),
        CoursesPage(
            imageRes = drawableImages[3],
            title = "Conceptos fundamentales",
            description =
                    "•\t Ingresos: dinero que recibes.\n" +
                    "•\t Gastos: dinero que gastas.\n" +
                    "•\t Ahorro: dinero que guardas para el futuro.\n" +
                    "•\t Inversión: dinero que pones a trabajar para ganar más.\n" +
                    "•\t Presupuesto: plan que te ayuda a controlar tus finanzas.\n" +
                    "•\t Deuda: dinero que debes a alguien.\n" +
                    "•\t Interés: costo de pedir dinero prestado o ganancia de invertir.\n",
        ),
        CoursesPage(
            imageRes = drawableImages[4],
            title = "Creando un presupuesto",
            description = "Un presupuesto es un plan que organiza tus ingresos y gastos. Es tu mapa financiero y te ayuda a:\n" +
                    "•\t Saber cuánto dinero tienes.\n" +
                    "•\t Controlar tus gastos.\n" +
                    "•\t Ahorrar para metas futuras.\n" +
                    "•\t Evitar deudas innecesarias.\n" +
                    "•\t Tomar decisiones informadas sobre tu dinero.\n"
        ),
    )
}
