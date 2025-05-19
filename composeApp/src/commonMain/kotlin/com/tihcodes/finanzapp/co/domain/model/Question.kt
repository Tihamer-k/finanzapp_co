package com.tihcodes.finanzapp.co.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)