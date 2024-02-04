package com.amv.simple.app.mysupernotes.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.NoteItemRepository

class NoteItemRepositoryImpl(
    private val application: Application
): NoteItemRepository{

    private val noteItemDao = AppDatabase.getInstance(application).noteDao()
    private val mapper = NoteItemMapper()

    override suspend fun addNoteItem(noteItem: NoteItem) {
        noteItemDao.addNoteItem(mapper.mapEntityToDbModel(noteItem))
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