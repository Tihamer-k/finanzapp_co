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
            content = "Este curso tiene como objetivo brindarte una base sólida sobre qué son las finanzas personales y cómo influyen en tu vida diaria. Las finanzas personales se refieren a cómo administras el dinero que ganas, gastas, ahorras e inviertes.",
            questions = listOf(
                Question(
                    text = "1.\t¿Por qué es importante crear un presupuesto?",
                    options = listOf(
                        "Solo es necesario si tienes deudas ",
                        "Es un requisito legal ",
                        "Te ayuda a organizar tus ingresos y gastos para tomar mejores decisiones",
                        "Solo sirve para empresas grandes"
                    ),
                    correctAnswer = "Te ayuda a organizar tus ingresos y gastos para tomar mejores decisiones"
                ),
                Question(
                    text = "2.\tJuan recibe \$10,000 semanales para el colegio y gasta \$7,000 en dulces y fotocopias. ¿Cuánto puede ahorrar al mes (4 semanas)?",
                    options = listOf(
                        "\$7,000",
                        "\$12,000",
                        "\$10,000",
                        "\$3,000"
                    ),
                    correctAnswer = "\$12,000"
                ),
                Question(
                    text = "3.\t¿Cuál de estos es un ejemplo de ingreso? ",
                    options = listOf(
                        "Comprar zapatos nuevos",
                        "Pagar la cuenta del teléfono",
                        "Guardar dinero en una alcancía ",
                        "Recibir dinero por ayudar en casa"
                    ),
                    correctAnswer = "Recibir dinero por ayudar en casa"
                )
            ),
        ),
        Course(
            "2", "Ahorro Inteligente", "Cómo ahorrar mejor", rewardId = "medal_2",
            content = "Ahorrar es separar una parte de tus ingresos para usarla en el futuro. No se trata solo de guardar lo que te sobra, sino de tener un plan. Ahorrar te permite enfrentar emergencias, alcanzar metas y evitar deudas.",
            questions = listOf(
                Question(
                    text = "1.\tSegún la regla 50/30/20, si recibes \$200,000, ¿cuánto deberías destinar al ahorro?",
                    options = listOf(
                        "\$50,000",
                        "\$40,000",
                        "\$60,000",
                        "\$100,000"
                    ),
                    correctAnswer = "\$40,000"
                ),
                Question(
                    text = "2.\t¿Cuál es la mejor estrategia para ahorrar efectivamente? ",
                    options = listOf(
                        "Guardar lo que sobra al final del mes",
                        "Pagar la renta",
                        "Ir al cine",
                        "Comprar videojuegos"
                    ),
                    correctAnswer = "Guardar lo que sobra al final del mes"
                ),
                Question(
                    text = "3.\t¿Qué es un \"gasto hormiga\"?",
                    options = listOf(
                        "Pequeños gastos diarios que sumados representan mucho dinero",
                        "Dinero que prestas y nunca recuperas",
                        "Un gasto muy grande que haces una vez al año",
                        "El costo de mantener una mascota"
                    ),
                    correctAnswer = "Pequeños gastos diarios que sumados representan mucho dinero"
                )
            ),
        ),
        Course(
            "3", "Inversiones Básicas", "Fundamentos de inversión", rewardId = "sim_1",
            content = "Invertir es usar tu dinero para generar más dinero. A diferencia del ahorro, las inversiones implican cierto riesgo, es decir, podrías ganar más, pero también podrías perder algo.",
            questions = listOf(
                Question(
                    text = "1.\t¿Qué es el interés compuesto? ",
                    options = listOf(
                        "Interés que ganas sobre tu inversión inicial y sobre los intereses anteriores",
                        "El costo que cobra un banco por abrir una cuenta",
                        "Una penalización por retirar tu dinero antes de tiempo",
                        "Un impuesto que pagas por tus inversiones"
                    ),
                    correctAnswer = "Interés que ganas sobre tu inversión inicial y sobre los intereses anteriores"
                ),
                Question(
                    text = "2.\t¿Por qué se dice que las acciones son más riesgosas que los bonos?",
                    options = listOf(
                        "Porque siempre pierdes dinero con las acciones",
                        "Porque los bonos siempre ganan más que las acciones",
                        "Porque el valor de las acciones puede fluctuar más, subiendo o bajando drásticamente",
                        "Porque las acciones tienen fechas de vencimiento más cortas"
                    ),
                    correctAnswer = "Porque el valor de las acciones puede fluctuar más, subiendo o bajando drásticamente"
                ),
                Question(
                    text = "3.\tLaura tiene \$1,000,000 para invertir. Quiere minimizar el riesgo pero aún así obtener algo de rendimiento. ¿Qué opción le recomendarías?",
                    options = listOf(
                        "Invertir todo en acciones de una sola empresa nueva",
                        "El ahorro no genera intereses, la inversión sí",
                        "Invertir en un fondo diversificado que incluya mayormente bonos",
                        "No hay diferencia"
                    ),
                    correctAnswer = "Invertir en un fondo diversificado que incluya mayormente bonos"
                )
            ),
        ),
        Course(
            "4", "Planificación Financiera", "Organiza tu futuro", rewardId = "sim_2",
            content = "La planificación financiera consiste en establecer metas claras y organizar tus recursos (dinero, tiempo, información) para alcanzarlas.",
            questions = listOf(
                Question(
                    text = "1.\t¿Qué significa que una meta financiera sea \"SMART\"?",
                    options = listOf(
                        "Que requiera mucho dinero para alcanzarla",
                        "Que sea Específica, Medible, Alcanzable, Relevante y con Tiempo definido",
                        "Que solo personas inteligentes puedan lograrla",
                        "Que utilice tecnología para su cumplimiento"
                    ),
                    correctAnswer = "Que sea Específica, Medible, Alcanzable, Relevante y con Tiempo definido"
                ),
                Question(
                    text = "2.\t¿Cuál de estos es un ejemplo de meta financiera a largo plazo?",
                    options = listOf(
                        "Crear un fondo de emergencia",
                        "Ahorrar para la universidad",
                        "Ahorrar para un concierto el próximo mes",
                        "Comprar un celular nuevo"
                    ),
                    correctAnswer = "Ahorrar para la universidad"
                ),
                Question(
                    text = "3.\tAntes de establecer otras metas financieras, ¿qué deberías asegurar primero?",
                    options = listOf(
                        "Comprar acciones en la bolsa de valores",
                        "Obtener una tarjeta de crédito",
                        "Tener un fondo de emergencia",
                        "Comprar una casa"
                    ),
                    correctAnswer = "Tener un fondo de emergencia"
                )
            ),
        )
    )
}