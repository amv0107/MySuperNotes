package com.amv.simple.app.mysupernotes.data

import androidx.room.TypeConverter
import com.amv.simple.app.mysupernotes.data.note.Attachment
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

object DatabaseConverters {

    @TypeConverter
    fun jsonFromAttachments(attachment: List<Attachment>): String {
        // TODO: Последовательность записи в json не соответствует структуре класса
        return Gson().toJson(attachment)
    }

    @TypeConverter
    fun attachmentsFromJson(json: String): List<Attachment> {
        return try {
            val listType = object : TypeToken<List<Attachment>>() {}.type
            Gson().fromJson(json, listType)
        } catch (e: Exception) {
            listOf()
        }
    }
}