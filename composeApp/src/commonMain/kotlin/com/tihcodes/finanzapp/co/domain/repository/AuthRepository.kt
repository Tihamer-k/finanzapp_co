package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

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
    suspend fun createUserFStore(
        name: String,
        surname: String,
        email: String,
        phone: String,
        password: String,
        date: String
    )
    suspend fun signOut()
    suspend fun resetPassword(email: String)
    suspend fun isExistingUser(email: String): Boolean
    suspend fun getUserData(userId: String): User


}
