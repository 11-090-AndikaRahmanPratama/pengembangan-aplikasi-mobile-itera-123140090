package com.newsfeed.myprofileapp.data
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.newsfeed.myprofileapp.db.NotesDatabase

object DatabaseProvider {
    @Volatile
    private var INSTANCE: NotesDatabase? = null

    fun getDatabase(context: Context): NotesDatabase {
        return INSTANCE ?: synchronized(this) {
            val driver = AndroidSqliteDriver(NotesDatabase.Schema, context, "notes.db")
            NotesDatabase(driver).also { INSTANCE = it }
        }
    }
}