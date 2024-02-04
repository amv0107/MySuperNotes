package com.amv.simple.app.mysupernotes.domain

import androidx.lifecycle.LiveData

interface NoteItemRepository {

    suspend fun addNoteItem(noteItem: NoteItem)
    suspend fun editNoteItem(noteItem: NoteItem)
    suspend fun getNoteItem(noteItemId: Int): NoteItem
    fun getNoteItemList(): LiveData<List<NoteItem>>
}