package com.amv.simple.app.mysupernotes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NoteItemDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteItemDao

    companion object {

        const val DB_NAME = "amv_simple_app_note.db"

    }
}