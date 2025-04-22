package com.tihcodes.finanzapp.co.data.local

import app.cash.sqldelight.db.SqlDriver
import com.finanzapp.UserDatabase
import com.tihcodes.finanzapp.co.data.User

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}