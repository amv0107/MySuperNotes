package com.amv.simple.app.mysupernotes.data

import androidx.lifecycle.LiveData
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.NoteItemRepository

class NoteItemRepositoryImpl(

): NoteItemRepository{

    override suspend fun addNoteItem(noteItem: NoteItem) {
        TODO("Not yet implemented")
    }

    override suspend fun editNoteItem(noteItem: NoteItem) {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteItem(noteItemId: Int): NoteItem {
        TODO("Not yet implemented")
    }

    override fun getNoteItemList(): LiveData<List<NoteItem>> {
        TODO("Not yet implemented")
    }
}