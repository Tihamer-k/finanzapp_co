package com.tihcodes.finanzapp.co

import android.app.Application
import com.tihcodes.finanzapp.co.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = { androidContext(this@MyApplication) }
        )
    }
}