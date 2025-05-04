package com.tihcodes.finanzapp.co.domain.model

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

data class NotificationItem(
    val id: String,
    val icon: DrawableResource,
    val title: String,
    val message: String,
    val dateTime: String,
    val categoryTag: String? = null,
    val categoryColor: Color? = null
)
