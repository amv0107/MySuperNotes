package com.amv.simple.app.mysupernotes.domain.note

import com.amv.simple.app.mysupernotes.domain.note.NoteItem
import com.amv.simple.app.mysupernotes.domain.note.NoteItemRepository
import javax.inject.Inject

class   UpdateNoteItemUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepository
) {
    suspend operator fun invoke(noteItem: NoteItem) =
        noteItemRepository.updateNoteItem(noteItem)
}