package com.tihcodes.finanzapp.co

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.tihcodes.finanzapp.co.ui.Navigation
import com.tihcodes.finanzapp.co.ui.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Theme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Navigation()
        }
    }
}