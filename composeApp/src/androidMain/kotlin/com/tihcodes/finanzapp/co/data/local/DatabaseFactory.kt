package com.tihcodes.finanzapp.co.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.finanzapp.UserDatabase

class AndroidDatabaseDriverFactory (
    private val context: Context
) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = UserDatabase.Schema,
            context = context,
            name = "user.db"
        )
    }
}