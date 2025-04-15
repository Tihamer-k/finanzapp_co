package com.tihcodes.finanzapp.co.service

import com.tihcodes.finanzapp.co.data.User
import kotlinx.coroutines.flow.Flow

interface AuthService {

    val currentUserId: String
    val isAuthenticated: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(
        email: String,
        password: String
    )
    suspend fun createUser(
        name: String,
        surname: String,
        email: String,
        phone: String,
        password: String,
        date: String
    )
    suspend fun signOut()
    suspend fun resetPassword(email: String)
}
