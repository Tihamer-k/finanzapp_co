package com.tihcodes.finanzapp.co.data.remote

import com.tihcodes.finanzapp.co.domain.model.NotificationItem
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore

class RemoteNotification {
    private val firestore = Firebase.firestore

    suspend fun getNotificationsByUserId(userId: String): List<NotificationItem> {
        return try {
            val documents = firestore.collection("notifications")
                .document(userId)
                .collection("userNotifications")
                .get()
            documents.documents.mapNotNull { it.toNotification() }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting notifications by UserId: ${e.message}")
            emptyList()
        }
    }

    suspend fun saveNotification(notification: NotificationItem) {
        try {
            val notificationValue = mapOf(
                "id" to notification.id,
                "icon" to notification.icon,
                "title" to notification.title,
                "message" to notification.message,
                "dateTime" to notification.dateTime,
                "categoryTag" to notification.categoryTag,
                "categoryColor" to notification.categoryColor,
                "isRead" to notification.isRead,
                "userId" to notification.userId
            )
            firestore.collection("notifications")
                .document(notification.userId)
                .collection("userNotifications")
                .document(notification.id)
                .set(notificationValue)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: saving notification: ${e.message}")
        }
    }

    suspend fun deleteNotification(notificationId: String) {
        try {
            firestore.collection("notifications")
                .document(notificationId)
                .delete()
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: deleting notification: ${e.message}")
        }
    }

    suspend fun markNotificationAsRead(notificationId: String) {
        try {
            firestore.collection("notifications")
                .document(notificationId)
                .update("isRead", true)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: marking notification as read: ${e.message}")
        }
    }

    private fun DocumentSnapshot.toNotification(): NotificationItem? {
        return try {
            NotificationItem(
                id = get("id") as String,
                icon = get("icon") as String,
                title = get("title") as String,
                message = get("message") as String,
                dateTime = get("dateTime") as String,
                categoryTag = get("categoryTag") as String?,
                categoryColor = get("categoryColor") as String?,
                isRead = get("isRead") as Boolean,
                userId = get("userId") as String
            )
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: parsing notification: ${e.message}")
            null
        }
    }
}
