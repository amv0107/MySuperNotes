package com.amv.simple.app.mysupernotes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.amv.simple.app.mysupernotes.domain.NoteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteItemDao {

    //TODO: NoteItem -> NoteItemDbModel
    @Query("SELECT * FROM note_items")
    fun getNoteItemList(): Flow<List<NoteItem>>

    @Insert
    suspend fun addNoteItem(noteItemDbModel: NoteItemDbModel)

    @Query("SELECT * FROM note_items WHERE id=:noteItemId LIMIT 1")
    fun getNoteItem(noteItemId: Int): Flow<NoteItemDbModel>

    @Update
    suspend fun updateNoteItem(noteItemDbModel: NoteItemDbModel)
}