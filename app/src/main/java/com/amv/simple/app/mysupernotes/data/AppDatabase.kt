package com.amv.simple.app.mysupernotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.amv.simple.app.mysupernotes.data.category.CategoryDbModel
import com.amv.simple.app.mysupernotes.data.category.CategoryItemDao
import com.amv.simple.app.mysupernotes.data.note.NoteItemDao
import com.amv.simple.app.mysupernotes.data.note.NoteItemDbModel
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

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

        fun getLocalizedDataFileName(context: Context): String {
            val locale = context.resources.configuration.locales.get(0)
            return when(locale.language) {
                "ru" -> "category_ru.json"
                "uk" -> "category_uk.json"
                "en" -> "category_en.json"
                else -> "category_en.json"
            }
        }

        fun readJsonData(context: Context, fileName: String): List<CategoryDbModel> {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            return Gson().fromJson(json, object: TypeToken<List<CategoryDbModel>>() {}.type)
        }

        const val DB_NAME = "amv_simple_app_note.db"

    }
}