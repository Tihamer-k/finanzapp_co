package com.tihcodes.finanzapp.co.data

data class Course(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isUnlocked: Boolean = false,
    val rewardId: String? = null // cada curso desbloquea una recompensa
)
