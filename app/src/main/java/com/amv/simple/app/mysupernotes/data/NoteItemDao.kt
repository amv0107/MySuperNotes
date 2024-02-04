package com.amv.simple.app.mysupernotes.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteItemDao {

    @Query("SELECT * FROM note_items")
    fun getNoteItemList(): LiveData<List<NoteItemDbModel>>

    @Insert
    suspend fun addNoteItem(noteItemDbModel: NoteItemDbModel)

    @Query("SELECT * FROM note_items WHERE id=:noteItemId LIMIT 1")
    suspend fun getNoteItem(noteItemId: Int): NoteItemDbModel
}