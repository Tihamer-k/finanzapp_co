package com.tihcodes.finanzapp.co.di

import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.IOSDatabaseDriverFactory
import com.tihcodes.finanzapp.co.utils.NotificationManager
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
    single<NotificationManager> {
        NotificationManager()
    }
    viewModel {
        com.tihcodes.finanzapp.co.presentation.viewmodel.NotificationViewModel(
            notificationManager = get()
        )
    }
}