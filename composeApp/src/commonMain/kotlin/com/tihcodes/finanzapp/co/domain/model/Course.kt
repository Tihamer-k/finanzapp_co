package com.tihcodes.finanzapp.co.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val content: String,
    val questions: List<Question> = emptyList(),
    var isCompleted: Boolean = false,
    val isUnlocked: Boolean = false,
    val rewardId: String? = null,
    var hasPendingQuestions: Boolean = true
)
