package com.tihcodes.finanzapp.co.data.repository

import com.tihcodes.finanzapp.co.data.local.LocalNotification
import com.tihcodes.finanzapp.co.data.remote.RemoteNotification
import com.tihcodes.finanzapp.co.domain.model.NotificationItem
import com.tihcodes.finanzapp.co.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val remoteDataSource: RemoteNotification,
    private val localDataSource: LocalNotification
) : NotificationRepository {
    override suspend fun getNotifications(userId: String): List<NotificationItem> {
        val localData = localDataSource.getNotificationsByUserId(userId)
        if (localData.isNotEmpty()) return localData
        val remoteData = remoteDataSource.getNotificationsByUserId(userId)
        if (remoteData.isNotEmpty()) {
            for (remoteItem in remoteData) {
                localDataSource.insertNotification(
                    id = remoteItem.id,
                    icon = remoteItem.icon,
                    title = remoteItem.title,
                    message = remoteItem.message,
                    dateTime = remoteItem.dateTime,
                    categoryTag = remoteItem.categoryTag,
                    categoryColor = remoteItem.categoryColor,
                    isRead = if (remoteItem.isRead) 1 else 0,
                    userId = remoteItem.userId
                )
            }
            return remoteData
        }
        return emptyList()
    }

    override suspend fun saveNotification(notification: NotificationItem) {
        localDataSource.insertNotification(
            id = notification.id,
            icon = notification.icon,
            title = notification.title,
            message = notification.message,
            dateTime = notification.dateTime,
            categoryTag = notification.categoryTag,
            categoryColor = notification.categoryColor,
            isRead = if (notification.isRead) 1 else 0,
            userId = notification.userId
        )
        remoteDataSource.saveNotification(notification)
    }

    override suspend fun deleteNotification(notificationId: String) {
        localDataSource.deleteNotificationById(notificationId)
        remoteDataSource.deleteNotification(notificationId)
    }

    override suspend fun markAsRead(notificationId: String) {
        localDataSource.markNotificationAsRead(notificationId)
        remoteDataSource.markNotificationAsRead(notificationId)
    }

    override suspend fun markAllAsRead() {
        // Se recomienda pasar el userId como parámetro si es necesario
        // localDataSource.markAllNotificationsAsRead(userId)
        // remoteDataSource.markAllNotificationsAsRead(userId)
    }
}