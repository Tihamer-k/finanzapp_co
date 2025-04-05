package com.tihcodes.finanzapp.co

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val osFamily: String = "Android"
}

actual fun getPlatform(): Platform = AndroidPlatform()