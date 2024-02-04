package com.amv.simple.app.mysupernotes.domain

class GetNoteItemUseCase(
    private val noteItemRepository: NoteItemRepository
) {

    suspend fun getNoteItem(noteItemId: Int): NoteItem {
        return noteItemRepository.getNoteItem(noteItemId)
    }
}