package com.tihcodes.finanzapp.co.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.domain.model.NotificationItem
import com.tihcodes.finanzapp.co.domain.repository.NotificationRepository
import com.tihcodes.finanzapp.co.presentation.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppNotificationViewModel(
    private val notificationRepository: NotificationRepository,
) : BaseViewModel() {

    private val _notifications = MutableStateFlow<List<NotificationItem>>(emptyList())
    val notifications: StateFlow<List<NotificationItem>> get() = _notifications

    val unreadCount: StateFlow<Int> = _notifications
        .map { it.count { notification -> !notification.isRead } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
    private val _idUser = MutableStateFlow("")


    init {
        loadNotifications(_idUser.value)
    }

    fun loadNotifications(userId: String) {
        viewModelScope.launch {
            _idUser.value = userId
            val notifications = notificationRepository.getNotifications(userId)
            _notifications.value = notifications
        }
    }

    fun setNotification(notification: NotificationItem) {
        viewModelScope.launch {
            val updatedNotification = notification.copy(userId = _idUser.value, isRead = false)

            // Actualizar estado local y remoto
            _notifications.value = listOf(updatedNotification) + _notifications.value
            notificationRepository.saveNotification(updatedNotification)
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            val updatedNotifications = _notifications.value.map {
                if (it.id == notificationId) it.copy(isRead = true) else it
            }
            _notifications.value = updatedNotifications

            // Actualizar estado en el repositorio
            notificationRepository.markAsRead(notificationId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            val updatedNotifications = _notifications.value.map { it.copy(isRead = true) }
            _notifications.value = updatedNotifications

            // Actualizar estado en el repositorio
            notificationRepository.markAllAsRead()
        }
    }

    fun removeNotification(notificationId: String) {
        viewModelScope.launch {
            _notifications.value = _notifications.value.filter { it.id != notificationId }

            // Eliminar notificación en el repositorio
            notificationRepository.deleteNotification(notificationId)
        }
    }
}
