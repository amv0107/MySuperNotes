package com.amv.simple.app.mysupernotes.domain.note

import com.amv.simple.app.mysupernotes.data.note.NoteItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesByCategoryUseCase @Inject constructor(
    private val noteRepository: NoteItemRepositoryImpl
) {

    operator fun invoke(categoryId: Int): Flow<List<NoteItem>> =
        noteRepository.getNotesByCategory(categoryId)

}