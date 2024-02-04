package com.amv.simple.app.mysupernotes.domain

import androidx.lifecycle.LiveData

class GetNoteListUseCase(
    private val noteItemRepository: NoteItemRepository
) {

    fun getNoteList(): LiveData<List<NoteItem>> {
       return noteItemRepository.getNoteItemList()
    }
}