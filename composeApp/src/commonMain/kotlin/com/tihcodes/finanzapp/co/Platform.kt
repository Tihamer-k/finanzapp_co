package com.tihcodes.finanzapp.co

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform