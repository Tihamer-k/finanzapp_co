package com.tihcodes.finanzapp.co.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Reward(
    val id: String,
    val name: String,
    val description: String,
    val type: RewardType,
    val isUnlocked: Boolean = false,
    @Transient
    val userId: String = "",
)