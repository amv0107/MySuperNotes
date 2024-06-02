package com.amv.simple.app.mysupernotes.data.note

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteItemDao {

    @Query("SELECT * FROM note_items")
    fun getNoteItemList(): Flow<List<NoteItemDbModel>>

    @Query("SELECT * FROM note_items WHERE categoryId = :categoryId")
    fun getNotesByCategory(categoryId: Int): Flow<List<NoteItemDbModel>>

    @Insert
    suspend fun addNoteItem(noteItemDbModel: NoteItemDbModel)

    @Query("SELECT * FROM note_items WHERE id=:noteItemId LIMIT 1")
    fun getNoteItem(noteItemId: Int): Flow<NoteItemDbModel>

    @Update
    suspend fun updateNoteItem(noteItemDbModel: NoteItemDbModel)

    @Delete
    suspend fun deleteForeverNoteItem(noteItemDbModel: NoteItemDbModel)
}