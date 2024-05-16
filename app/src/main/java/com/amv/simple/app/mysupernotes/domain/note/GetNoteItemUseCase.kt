package com.amv.simple.app.mysupernotes.domain.note

import com.amv.simple.app.mysupernotes.data.note.NoteItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteItemUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    operator fun invoke(noteItemId: Int): Flow<NoteItem> =
        noteItemRepository.getNoteItem(noteItemId)

}