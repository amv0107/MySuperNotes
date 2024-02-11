package com.amv.simple.app.mysupernotes.domain

import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import javax.inject.Inject

class AddNoteItemUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    suspend operator fun invoke(noteItem: NoteItem) =
        noteItemRepository.addNoteItem(noteItem)

}