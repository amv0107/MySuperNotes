package com.amv.simple.app.mysupernotes.domain

import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import javax.inject.Inject

class DeleteForeverNoteItemUseCase @Inject constructor(
    private val repository: NoteItemRepositoryImpl
) {
    suspend operator fun invoke(noteItem: NoteItem) =
        repository.deleteForeverNoteItem(noteItem)
}