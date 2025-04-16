package com.tihcodes.finanzapp.co.service.impl


import com.tihcodes.finanzapp.co.data.User
import com.tihcodes.finanzapp.co.service.AuthService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthServiceImpl(
    private val auth: FirebaseAuth,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : AuthService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

    override val currentUser: Flow<User> =
        auth.authStateChanged.map { it?.let { User(it.uid, it.isAnonymous) } ?: User() }

    private suspend fun launchWithAwait(block: suspend () -> Unit) {
        scope.async {
            block()
        }.await()
    }

    override suspend fun authenticate(email: String, password: String) {
        launchWithAwait {
            auth.signInWithEmailAndPassword(email, password)
        }
    }

    override suspend fun createUser(
        name: String,
        surname: String,
        email: String,
        phone: String,
        password: String,
        date: String
    ) {
        launchWithAwait {
            val user = auth.createUserWithEmailAndPassword(email, password)
            user.let {
                val userId = it.user?.uid
                val database = Firebase.database
                val usersRef = database.reference("users")

                val userData = mapOf(
                    "name" to name,
                    "surname" to surname,
                    "phone" to phone,
                    "date" to date,
                    "email" to email,
                )
                if (userId != null) {
                    usersRef.child(userId).setValue(userData)
                }
            }
        }
    }

    override suspend fun signOut() {

        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        auth.signOut()
    }

    override suspend fun resetPassword(email: String) {
        launchWithAwait {
            auth.sendPasswordResetEmail(email)
        }
    }

    override suspend fun isExistingUser(email: String): Boolean {
        return try {
            val signInMethods = auth.fetchSignInMethodsForEmail(email)
            signInMethods.isNotEmpty() // If there are any sign-in methods, the user exists.
        } catch (e: Exception) {
            false // Handle exceptions, possibly network errors or invalid email format.
        }
    }
}