package com.amv.simple.app.mysupernotes.domain.note

import javax.inject.Inject

class UpdateNoteItemUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepository
) {
    suspend operator fun invoke(noteItem: NoteItem) =
        noteItemRepository.updateNoteItem(noteItem)
}