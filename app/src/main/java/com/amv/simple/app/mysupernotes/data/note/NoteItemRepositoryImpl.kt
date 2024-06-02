package com.amv.simple.app.mysupernotes.data.note

import com.amv.simple.app.mysupernotes.domain.note.NoteItem
import com.amv.simple.app.mysupernotes.domain.note.NoteItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteItemRepositoryImpl @Inject constructor(
    private val dao: NoteItemDao,
    private val mapper: NoteItemMapper
) : NoteItemRepository {

    override suspend fun addNoteItem(noteItem: NoteItem) =
        dao.addNoteItem(mapper.mapEntityToDbModel(noteItem))

    override suspend fun editNoteItem(noteItem: NoteItem) {}

    override fun getNoteItem(noteItemId: Int): Flow<NoteItem> =
        dao.getNoteItem(noteItemId).map { noteItemDbModel ->
            mapper.mapDbModelToEntity(noteItemDbModel)
        }

    override fun getNoteItemList(): Flow<List<NoteItem>> = dao.getNoteItemList().map {
        mapper.mapListDbModelToListEntity(it)
    }

    override fun getNotesByCategory(categoryId: Int): Flow<List<NoteItem>> = dao.getNotesByCategory(categoryId).map {
        mapper.mapListDbModelToListEntity(it)
    }

    override suspend fun updateNoteItem(noteItem: NoteItem) =
        dao.updateNoteItem(mapper.mapEntityToDbModel(noteItem))

    override suspend fun deleteForeverNoteItem(noteItem: NoteItem) =
        dao.deleteForeverNoteItem(mapper.mapEntityToDbModel(noteItem))

}