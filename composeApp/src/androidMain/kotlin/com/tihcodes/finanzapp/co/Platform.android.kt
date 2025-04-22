package com.tihcodes.finanzapp.co

import android.os.Build
import com.tihcodes.finanzapp.co.data.local.AndroidDatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val osFamily: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual val targetModule = module {
    single<DatabaseDriverFactory> {
        AndroidDatabaseDriverFactory(androidContext())
    }
}