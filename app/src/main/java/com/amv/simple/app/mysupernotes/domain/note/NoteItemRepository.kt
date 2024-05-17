package com.amv.simple.app.mysupernotes.domain.note

import kotlinx.coroutines.flow.Flow

interface NoteItemRepository {

    suspend fun addNoteItem(noteItem: NoteItem)
    suspend fun editNoteItem(noteItem: NoteItem)
    suspend fun updateNoteItem(noteItem: NoteItem)
    suspend fun deleteForeverNoteItem(noteItem: NoteItem)
    fun getNoteItem(noteItemId: Int): Flow<NoteItem>
    fun getNoteItemList(): Flow<List<NoteItem>>
}