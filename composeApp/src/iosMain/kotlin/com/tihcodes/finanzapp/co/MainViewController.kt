package com.tihcodes.finanzapp.co

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }