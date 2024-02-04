package com.amv.simple.app.mysupernotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "note_items"
)
data class NoteItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val textContent: String,
    val date: String,
)
