package com.tihcodes.finanzapp.co.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class NotificationItem(
    val id: String,
    val icon: String,
    val title: String,
    val message: String,
    val dateTime: String,
    val categoryTag: String? = null,
    val categoryColor: String? = null,
    val isRead: Boolean = false,
    @Transient
    val userId: String = "",
)

