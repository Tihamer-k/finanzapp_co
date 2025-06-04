package com.tihcodes.finanzapp.co.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.presentation.common.BaseViewModel
import com.tihcodes.finanzapp.co.utils.NotificationManager
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationManager: NotificationManager
) : BaseViewModel() {
    fun executeNotification(title: String, description: String) {
        viewModelScope.launch {
            notificationManager.showNotification(
                title = title,
                description = description
            )
        }
    }
}
