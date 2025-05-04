package com.tihcodes.finanzapp.co.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class BottomNavItem(
    val title: String,
    val icon: DrawableResource,
    val route: String,
)
