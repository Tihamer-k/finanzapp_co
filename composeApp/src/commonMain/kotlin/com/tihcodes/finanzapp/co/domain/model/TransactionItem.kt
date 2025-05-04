package com.tihcodes.finanzapp.co.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
data class TransactionItem(
    val id: String,
    val title: String,
    val category: String,
    val date: LocalDate,
    val amount: Double,
    val type: TransactionType,
    @Contextual val icon: DrawableResource,
    val userId: String = "" // Default empty string for backward compatibility
)
