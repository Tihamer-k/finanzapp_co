package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.data.User

class UserDatabase(
    databaseDriverFactory: DatabaseDriverFactory
){
    private val database = Database(
        driver = databaseDriverFactory.createDriver()
    )
    private val userQueries = database.userQueries

    fun getUserById(userId: String) : migrations.User? {
        println("INFO: Reading user data from database")
        return userQueries.getUserById(
            id = userId
        ).executeAsOneOrNull().let { user ->
            if (user != null) {
                migrations.User(
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