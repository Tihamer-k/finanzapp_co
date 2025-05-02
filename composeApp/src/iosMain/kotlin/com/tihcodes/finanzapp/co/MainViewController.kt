package com.tihcodes.finanzapp.co

import androidx.compose.ui.window.ComposeUIViewController
import com.tihcodes.finanzapp.co.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }