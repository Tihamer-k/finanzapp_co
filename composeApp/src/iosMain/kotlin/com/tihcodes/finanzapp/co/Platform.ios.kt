package com.tihcodes.finanzapp.co

import com.tihcodes.finanzapp.co.data.local.DatabaseDriverFactory
import com.tihcodes.finanzapp.co.data.local.IOSDatabaseDriverFactory
import org.koin.dsl.module
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val osFamily: String = "iOS"
}

actual fun getPlatform(): Platform = IOSPlatform()

actual val targetModule = module {
    single<DatabaseDriverFactory> {
        IOSDatabaseDriverFactory()
    }
}