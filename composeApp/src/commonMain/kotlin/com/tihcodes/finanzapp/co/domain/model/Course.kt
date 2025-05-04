package com.tihcodes.finanzapp.co.domain.model

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false,
    val rewardId: String? = null,
    val hasPendingQuestions: Boolean = true // nuevo campo para preguntas pendientes
)
