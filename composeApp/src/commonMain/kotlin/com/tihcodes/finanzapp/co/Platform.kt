package com.tihcodes.finanzapp.co

interface Platform {
    val name: String
    val osFamily: String
}

expect fun getPlatform(): Platform