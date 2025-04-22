package com.tihcodes.finanzapp.co

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.tihcodes.finanzapp.co.ui.Navigation
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel
import com.tihcodes.finanzapp.co.ui.theme.Theme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    Theme {
        val authViewModel = koinViewModel<AuthViewModel>()
        Surface(color = MaterialTheme.colorScheme.background) {
            Navigation(authViewModel)
        }
    }
}