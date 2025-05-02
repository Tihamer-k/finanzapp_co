package com.tihcodes.finanzapp.co.data.local

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.finanzapp.Database

class AndroidDatabaseDriverFactory (
    private val context: Context
) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        // Delete the database file if it exists to force recreation
        // context.getDatabasePath("Database.db").delete()

        return AndroidSqliteDriver(
            schema = Database.Schema,
            context = context,
            name = "Database.db"
        )
    }
}
