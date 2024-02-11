package com.amv.simple.app.mysupernotes.domain

import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    fun getNoteList(): Flow<List<NoteItem>> {
       return noteItemRepository.getNoteItemList()
    }
}