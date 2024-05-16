package com.amv.simple.app.mysupernotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amv.simple.app.mysupernotes.data.category.CategoryDbModel
import com.amv.simple.app.mysupernotes.data.category.CategoryItemDao
import com.amv.simple.app.mysupernotes.data.note.NoteItemDao
import com.amv.simple.app.mysupernotes.data.note.NoteItemDbModel

@Database(
    entities = [
        NoteItemDbModel::class,
        CategoryDbModel::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteItemDao
    abstract fun categoryDao(): CategoryItemDao

    companion object {

        const val DB_NAME = "amv_simple_app_note.db"

    }
}