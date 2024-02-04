package com.amv.simple.app.mysupernotes.domain

class AddNoteItemUseCase(
    private val noteItemRepository: NoteItemRepository
) {

    suspend operator fun invoke(noteItem: NoteItem) =
        noteItemRepository.addNoteItem(noteItem)

}