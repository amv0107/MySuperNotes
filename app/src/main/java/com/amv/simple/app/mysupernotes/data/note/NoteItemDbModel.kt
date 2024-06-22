package com.amv.simple.app.mysupernotes.data.note

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.amv.simple.app.mysupernotes.data.category.CategoryDbModel

@Entity(
    tableName = "note_items",
)
data class NoteItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val textContent: String,
    val dateOfCreate: Long,
    val attachment: List<Attachment>,
    val isPined: Int,
    val isFavorite: Int,
    val isArchive: Int,
    val isDelete: Int,
    val deletionDate: Long,
    val categoryId: Int,
)
