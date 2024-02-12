package com.amv.simple.app.mysupernotes.data

import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.NoteItemRepository
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

    override suspend fun updateNoteItem(noteItem: NoteItem) =
        dao.updateNoteItem(mapper.mapEntityToDbModel(noteItem))
}