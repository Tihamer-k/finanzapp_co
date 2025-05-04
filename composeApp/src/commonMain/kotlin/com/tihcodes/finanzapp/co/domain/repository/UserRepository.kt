package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    fun getUserById(id: String): Flow<User?>
//    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}