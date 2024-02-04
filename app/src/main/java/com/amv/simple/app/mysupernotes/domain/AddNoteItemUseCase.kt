package com.amv.simple.app.mysupernotes.domain

class AddNoteItemUseCase(
    private val noteItemRepository: NoteItemRepository
) {
    suspend fun addNoteItem(noteItem: NoteItem){
        noteItemRepository.addNoteItem(noteItem)
    }
}