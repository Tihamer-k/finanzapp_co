package com.tihcodes.finanzapp.co.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phone: String = "",
    val date: String = "",
    val isAnonymous: Boolean = true
)