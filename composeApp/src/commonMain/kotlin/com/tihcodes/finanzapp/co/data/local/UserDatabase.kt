package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.UserDatabase
import com.tihcodes.finanzapp.co.data.User

class UserDatabase(
    databaseDriverFactory: DatabaseDriverFactory
){
    private val database = UserDatabase(
        driver = databaseDriverFactory.createDriver()
    )
    private val userQueries = database.userDatabaseQueries

    fun getUserById(userId: String) : com.finanzapp.User? {
        println("INFO: Reading user data from database")
        return userQueries.getUserById(
            id = userId
        ).executeAsOneOrNull().let { user ->
            if (user != null) {
                com.finanzapp.User(
                    id = user.id,
                    name = user.name,
                    surname = user.surname,
                    email = user.email,
                    phone = user.phone,
                    date = user.date,
                    isAnonymous = user.isAnonymous
                )
            } else {
                null
            }
        }
    }

    fun insertUser(user: User) {
        println("INFO: Inserting user data into database")
        userQueries.insertUser(
            id = user.id,
            name = user.name,
            surname = user.surname,
            email = user.email,
            phone = user.phone,
            date = user.date,
            isAnonymous = 0,
        )
    }


    fun updateUser(user: User) {
        println("INFO: Updating user data in database")
        userQueries.updateUser(
            id = user.id,
            name = user.name,
            surname = user.surname,
            email = user.email,
            phone = user.phone,
            date = user.date,
            isAnonymous = 0
        )
    }
}