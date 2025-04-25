package com.tihcodes.finanzapp.co.di

import com.tihcodes.finanzapp.co.data.local.AndroidDatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}