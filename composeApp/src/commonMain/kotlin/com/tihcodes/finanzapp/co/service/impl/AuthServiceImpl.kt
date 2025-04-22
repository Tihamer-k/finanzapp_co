package com.tihcodes.finanzapp.co.service.impl


import com.russhwolf.settings.Settings
import com.tihcodes.finanzapp.co.data.User
import com.tihcodes.finanzapp.co.data.local.UserDatabase
import com.tihcodes.finanzapp.co.service.AuthService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.database
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthServiceImpl(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
    settings: Settings,
    userDatabase: UserDatabase,
) : AuthService {

    private val userDatabaseDriver = userDatabase

    private val userSettings = settings

    private val userRepository = UserServiceImpl()

    override val currentUserId: String
        get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean
        get() = auth.currentUser != null && auth.currentUser?.isAnonymous == false

    override val currentUser: Flow<User> =
        auth.authStateChanged.map { it?.let { User(it.uid, it.isAnonymous.toString()) } ?: User() }

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

    override suspend fun createUserFStore(
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
                val database = database.collection("users")

                val userData = mapOf(
                    "name" to name,
                    "surname" to surname,
                    "phone" to phone,
                    "date" to date,
                    "email" to email,
                )
                if (userId != null) {
                    database.document(userId).set(userData)
                    userDatabaseDriver.insertUser(
                        User(
                            id = userId,
                            name = name,
                            surname = surname,
                            email = email,
                            phone = phone,
                            date = date
                        )
                    )
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

//    override suspend fun getUserData(userId: String): User {
//        return try {
//            userRepository.getUserById(userId).map { user ->
//                User(
//                    id = user.id,
//                    name = user.name,
//                    surname = user.surname,
//                    email = user.email,
//                    phone = user.phone,
//                    date = user.date
//                )
//            }.first() // Collect the first emitted value from the Flow
//        } catch (e: Exception) {
//            User()
//        }
//    }

    override suspend fun getUserData(userId: String): User {
        return try {
            // Primero intentar obtener usuario de la base de datos local
            val localUser = userDatabaseDriver.getUserById(userId)

            if (localUser != null) {
                println("INFO: Usuario local encontrado: $localUser")
                // Convertir de com.finanzapp.User a com.tihcodes.finanzapp.co.data.User
                User(
                    id = localUser.id,
                    name = localUser.name ?: "",
                    surname = localUser.surname ?: "",
                    email = localUser.email ?: "",
                    phone = localUser.phone ?: "",
                    date = localUser.date ?: "",
                    isAnonymous = localUser.isAnonymous == 1L
                )
            } else {
                println("INFO: Usuario local no encontrado, intentando obtener de Firestore")
                // Si no está en local, intentar obtener de Firestore
                userRepository.getUserById(userId).first()
            }
        } catch (e: Exception) {
            println("ERROR: Error al obtener datos de usuario: ${e.message}")
            User() // Devolver un usuario vacío en caso de error
        }
    }
}

