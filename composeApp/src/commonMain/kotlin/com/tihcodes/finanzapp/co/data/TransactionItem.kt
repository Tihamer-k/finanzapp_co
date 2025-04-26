package com.tihcodes.finanzapp.co.data

import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource

data class TransactionItem(
    val id: String,
    val title: String,
    val category: String,
    val date: LocalDate,
    val amount: Double,
    val type: TransactionType,
    val icon: DrawableResource
)