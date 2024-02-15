package com.amv.simple.app.mysupernotes.domain

import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    fun getNoteList(archiveNotes: Boolean = false): Flow<List<NoteItem>> {
        return noteItemRepository.getNoteItemList().map { list ->
            list
                .filter { it.isArchive == archiveNotes }
                .sortedByDescending { item ->
                item.isPinned
            }
        }
    }
}