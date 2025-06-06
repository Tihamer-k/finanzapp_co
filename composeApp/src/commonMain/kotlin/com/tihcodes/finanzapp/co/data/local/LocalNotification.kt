package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.domain.model.NotificationItem

class LocalNotification(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(
        driver = databaseDriverFactory.createDriver()
    )
    private val notificationQueries = database.notificationQueries

    fun getNotificationsByUserId(userId: String): List<NotificationItem> {
        println("INFO: Reading notifications data from database")
        return try {
            notificationQueries.getNotificationsByUserId(userId).executeAsList().map { notification ->
                NotificationItem(
                    id = notification.id,
                    icon = notification.icon ?: "",
                    title = notification.title,
                    message = notification.message,
                    dateTime = notification.dateTime,
                    categoryTag = notification.categoryTag,
                    categoryColor = notification.categoryColor,
                    isRead = notification.isRead.toInt() == 1,
                    userId = notification.userId
                )
            }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
            emptyList()
        }
    }

    fun insertNotification(
        id: String,
        icon: String,
        title: String,
        message: String,
        dateTime: String,
        categoryTag: String?,
        categoryColor: String?,
        isRead: Long,
        userId: String
    ) {
        try {
            notificationQueries.insertNotification(
                id = id,
                icon = icon,
                title = title,
                message = message,
                dateTime = dateTime,
                categoryTag = categoryTag,
                categoryColor = categoryColor,
                isRead = isRead,
                userId = userId
            )
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
        }
    }

    fun deleteNotificationById(notificationId: String) {
        try {
            notificationQueries.deleteNotificationById(notificationId)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
        }
    }

    fun markNotificationAsRead(notificationId: String) {
        try {
            notificationQueries.markNotificationAsRead(notificationId)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
        }
    }

    fun markAllNotificationsAsRead(userId: String) {
        try {
            notificationQueries.markAllNotificationsAsRead(userId)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
        }
    }
}
