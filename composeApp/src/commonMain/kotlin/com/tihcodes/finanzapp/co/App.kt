package com.tihcodes.finanzapp.co

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.tihcodes.finanzapp.co.service.impl.AuthServiceImpl
import com.tihcodes.finanzapp.co.ui.Navigation
import com.tihcodes.finanzapp.co.ui.screens.model.AuthViewModel
import com.tihcodes.finanzapp.co.ui.theme.Theme
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Theme {
        val authViewModel = AuthViewModel(AuthServiceImpl(auth = Firebase.auth))
        Surface(color = MaterialTheme.colorScheme.background) {
            Navigation(authViewModel)
        }
    }
}