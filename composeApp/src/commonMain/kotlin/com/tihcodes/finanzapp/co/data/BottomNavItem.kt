package com.tihcodes.finanzapp.co.data

import org.jetbrains.compose.resources.DrawableResource

data class BottomNavItem(
    val title: String,
    val icon: DrawableResource,
    val route: String,
)
