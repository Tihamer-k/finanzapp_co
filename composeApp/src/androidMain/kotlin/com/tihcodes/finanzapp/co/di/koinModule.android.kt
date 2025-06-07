package com.tihcodes.finanzapp.co.di

import com.tihcodes.finanzapp.co.data.local.AndroidDatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import com.tihcodes.finanzapp.co.utils.NotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
    single<NotificationManager> {
        NotificationManager(
            context = androidContext()
        )
    }
    viewModel {
        com.tihcodes.finanzapp.co.presentation.viewmodel.NotificationViewModel(
            notificationManager = get()
        )
    }
}