package com.amv.simple.app.mysupernotes.data.note

import com.amv.simple.app.mysupernotes.domain.note.NoteItem

class NoteItemMapper {

    fun mapEntityToDbModel(noteItem: NoteItem) = NoteItemDbModel(
        id = noteItem.id,
        title = noteItem.title,
        textContent = noteItem.textContent,
        dateOfCreate = noteItem.dateOfCreate,
        isPined = if (noteItem.isPinned) 1 else 0,
        isFavorite = if (noteItem.isFavorite) 1 else 0,
        isArchive = if(noteItem.isArchive) 1 else 0,
        isDelete = if(noteItem.isDelete) 1 else 0,
        deletionDate = noteItem.deletionDate,
    )


    fun mapDbModelToEntity(noteItemDbModel: NoteItemDbModel) = NoteItem(
        id = noteItemDbModel.id,
        title = noteItemDbModel.title,
        textContent = noteItemDbModel.textContent,
        dateOfCreate = noteItemDbModel.dateOfCreate,
        isPinned = noteItemDbModel.isPined != 0,
        isFavorite = noteItemDbModel.isFavorite != 0,
        isArchive = noteItemDbModel.isArchive != 0,
        isDelete = noteItemDbModel.isDelete != 0,
        deletionDate = noteItemDbModel.deletionDate,
    )

    fun mapListDbModelToListEntity(list: List<NoteItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}