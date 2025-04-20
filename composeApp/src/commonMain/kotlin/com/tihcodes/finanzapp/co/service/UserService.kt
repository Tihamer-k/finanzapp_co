package com.tihcodes.finanzapp.co.service

import com.tihcodes.finanzapp.co.data.User
import kotlinx.coroutines.flow.Flow

interface UserService {
    fun getUsers(): Flow<List<User>>
    fun getUserById(id: String): Flow<User?>
//    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}