package com.tihcodes.finanzapp.co.domain.model

data class Reward(
    val id: String,
    val name: String,
    val description: String,
    val type: RewardType,
    val isUnlocked: Boolean = false
)