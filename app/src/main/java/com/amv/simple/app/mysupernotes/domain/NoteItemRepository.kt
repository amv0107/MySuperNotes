package com.amv.simple.app.mysupernotes.domain

import kotlinx.coroutines.flow.Flow

interface NoteItemRepository {

    suspend fun addNoteItem(noteItem: NoteItem)
    suspend fun editNoteItem(noteItem: NoteItem)
    fun getNoteItem(noteItemId: Int): Flow<NoteItem>
    fun getNoteItemList(): Flow<List<NoteItem>>
}