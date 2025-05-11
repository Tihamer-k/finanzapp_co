package com.tihcodes.finanzapp.co.presentation.screen.learn

import com.tihcodes.finanzapp.co.domain.model.Course
import com.tihcodes.finanzapp.co.domain.model.Question

fun getCourses(): List<Course> {
    return listOf(
        Course(
            "1",
            "Introducción a Finanzas",
            "Conceptos básicos",
            isUnlocked = true,
            rewardId = "medal_1",
            content = "Este curso tiene como objetivo brindarte una base sólida sobre qué son las finanzas personales y cómo influyen en tu vida diaria. Las finanzas personales se refieren a cómo administras el dinero que ganas, gastas, ahorras e inviertes.\n" +
                    "Aprenderás a identificar tus ingresos (todo el dinero que recibes), tus gastos (todo lo que pagas o consumes), tu capacidad de ahorro, y la importancia de planificar lo que haces con tu dinero. Además, conocerás qué es un presupuesto, una herramienta fundamental que te ayuda a tener el control de tus finanzas, organizando tus ingresos y gastos para evitar endeudarte o gastar más de lo que tienes.",
            questions = listOf(
                Question(
                    text = "¿Qué es una tasa de interés?",
                    options = listOf(
                        "Un impuesto",
                        "Una tarifa",
                        "El costo del dinero",
                        "Una multa"
                    ),
                    correctAnswer = "El costo del dinero"
                ),
                Question(
                    text = "¿Qué es una tasa de interés?",
                    options = listOf(
                        "Un impuesto",
                        "Una tarifa",
                        "El costo del dinero",
                        "Una multa"
                    ),
                    correctAnswer = "El costo del dinero"
                )
            ),
        ),
        Course(
            "2", "Ahorro Inteligente", "Cómo ahorrar mejor", rewardId = "medal_2",
            content = "Ahorrar es separar una parte de tus ingresos para usarla en el futuro. No se trata solo de guardar lo que te sobra, sino de tener un plan. Ahorrar te permite enfrentar emergencias, alcanzar metas y evitar deudas.\n" +
                    "Aprenderás estrategias como la regla 50/30/20, donde:\n" +
                    "\n" +
                    "El 50% de tu dinero se destina a necesidades (comida, vivienda, transporte).\n" +
                    "\n" +
                    "El 30% a deseos (salidas, entretenimiento).\n" +
                    "\n" +
                    "El 20% se debe ahorrar o usar para pagar deudas.\n" +
                    "También conocerás los gastos hormiga, que son esos pequeños gastos diarios (como una bebida o snack) que parecen insignificantes pero que, acumulados, pueden afectar tu capacidad de ahorro.",
            questions = listOf(
                Question(
                    text = "¿Qué es una tasa de interés?",
                    options = listOf(
                        "Un impuesto",
                        "Una tarifa",
                        "El costo del dinero",
                        "Una multa"
                    ),
                    correctAnswer = "El costo del dinero"
                )
            ),
        ),
        Course(
            "3", "Inversiones Básicas", "Fundamentos de inversión", rewardId = "sim_1",
            content = "nvertir es usar tu dinero para generar más dinero. A diferencia del ahorro, las inversiones implican cierto riesgo, es decir, podrías ganar más, pero también podrías perder algo.\n" +
                    "En este curso conocerás la relación entre riesgo y rendimiento: a mayor potencial de ganancia, mayor es el riesgo.\n" +
                    "Verás los principales instrumentos de inversión, como:\n" +
                    "\n" +
                    "Acciones: comprar una parte de una empresa.\n" +
                    "\n" +
                    "Bonos: prestar dinero a una empresa o gobierno a cambio de intereses.\n" +
                    "\n" +
                    "Fondos de inversión: agrupaciones de dinero de muchas personas que invierten en distintos activos.\n" +
                    "Además, aprenderás por qué comenzar a invertir temprano es una ventaja, gracias al interés compuesto, que hace crecer tu inversión a lo largo del tiempo.",
            questions = listOf(
                Question(
                    text = "¿Qué es una tasa de interés?",
                    options = listOf(
                        "Un impuesto",
                        "Una tarifa",
                        "El costo del dinero",
                        "Una multa"
                    ),
                    correctAnswer = "El costo del dinero"
                )
            ),
        ),
        Course(
            "4", "Planificación Financiera", "Organiza tu futuro", rewardId = "sim_2",
            content = "La planificación financiera consiste en establecer metas claras y organizar tus recursos (dinero, tiempo, información) para alcanzarlas. Una meta financiera puede ser ahorrar para comprar algo, viajar, estudiar o formar un negocio.\n" +
                    "Este curso te enseñará a identificar metas a corto, mediano y largo plazo, y a trazar un plan con fechas, cantidades y estrategias para lograr cada una.\n" +
                    "También verás la importancia de tener un fondo de emergencia, un dinero reservado para imprevistos como una enfermedad o pérdida de ingresos.\n" +
                    "Planificar tus finanzas desde joven te da más libertad y seguridad en el futuro.\n" +
                    "\n",
            questions = listOf(
                Question(
                    text = "¿Qué es una tasa de interés?",
                    options = listOf(
                        "Un impuesto",
                        "Una tarifa",
                        "El costo del dinero",
                        "Una multa"
                    ),
                    correctAnswer = "El costo del dinero"
                )
            ),
        )
    )
}