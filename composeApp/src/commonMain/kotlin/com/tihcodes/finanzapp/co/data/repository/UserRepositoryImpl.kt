package com.tihcodes.finanzapp.co.data.repository

import com.tihcodes.finanzapp.co.domain.model.User
import com.tihcodes.finanzapp.co.domain.repository.UserRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl: UserRepository {

    private val firestore = Firebase.firestore

    override fun getUsers() = flow {
        firestore.collection("users").snapshots.collect { querySnapshot ->
            val users = querySnapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<User>()
            }
            emit(users)
        }
    }

    override fun getUserById(id: String) = flow {
        firestore.collection("users").document(id).snapshots.collect { documentSnapshot ->
            emit(documentSnapshot.data<User>())
        }
    }

//    override suspend fun addUser(user: User) {
//        val userId = generateRandomStringId()
//        firestore.collection("users")
//            .document(userId)
//            .set(user.copy(id = userId))
//    }

    override suspend fun updateUser(user: User) {
        firestore.collection("users").document(user.id).set(user)
    }

    override suspend fun deleteUser(user: User) {
        firestore.collection("users").document(user.id).delete()
    }
}

