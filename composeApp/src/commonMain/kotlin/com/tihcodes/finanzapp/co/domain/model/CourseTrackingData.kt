package com.tihcodes.finanzapp.co.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseTrackingData(
    val id: String,
    val title: String,
    val isCompleted: Long,
    val isUnlocked: Long,
    val userId: String,
    val rewardId: String? = null,
    var hasPendingQuestions: Long = 1,
)