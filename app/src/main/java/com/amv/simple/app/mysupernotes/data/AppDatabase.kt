package com.amv.simple.app.mysupernotes.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amv.simple.app.mysupernotes.data.whatsNew.WhatsNewDao
import com.amv.simple.app.mysupernotes.data.whatsNew.WhatsNewDbModel

@Database(
    entities = [
        NoteItemDbModel::class,
        WhatsNewDbModel::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteItemDao
    abstract fun whatsNewDao(): WhatsNewDao

    companion object {

        const val DB_NAME = "amv_simple_app_note.db"

        val migration1To2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS whats_new_items (versionName TEXT NOT NULL PRIMARY KEY, description TEXT NOT NULL)")
                database.execSQL("INSERT INTO whats_new_items (versionName,description) VALUE ('1.2.0', 'bugfix')")
            }
        }

    }
}