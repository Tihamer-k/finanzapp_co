package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.domain.model.NotificationItem

interface NotificationRepository {
    suspend fun getNotifications(userId: String): List<NotificationItem>
    suspend fun saveNotification(notification: NotificationItem)
    suspend fun deleteNotification(notificationId: String)
    suspend fun markAsRead(notificationId: String)
    suspend fun markAllAsRead()
}
