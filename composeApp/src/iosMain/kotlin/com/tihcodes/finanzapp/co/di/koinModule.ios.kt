package com.tihcodes.finanzapp.co.di

import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.IOSDatabaseDriverFactory
import org.koin.dsl.module

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}