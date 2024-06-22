package com.amv.simple.app.mysupernotes.data.note

import com.amv.simple.app.mysupernotes.domain.note.NoteItem

class NoteItemMapper {

    fun mapEntityToDbModel(noteItem: NoteItem) = NoteItemDbModel(
        id = noteItem.id,
        title = noteItem.title,
        textContent = noteItem.textContent,
        attachment = noteItem.attachment,
        dateOfCreate = noteItem.dateOfCreate,
        isPined = mapBooleanToInt(noteItem.isPinned),
        isFavorite = mapBooleanToInt(noteItem.isFavorite),
        isArchive = mapBooleanToInt(noteItem.isArchive),
        isDelete = mapBooleanToInt(noteItem.isDelete),
        deletionDate = noteItem.deletionDate,
        categoryId = noteItem.categoryId,
    )


    fun mapDbModelToEntity(noteItemDbModel: NoteItemDbModel) = NoteItem(
        id = noteItemDbModel.id,
        title = noteItemDbModel.title,
        textContent = noteItemDbModel.textContent,
        dateOfCreate = noteItemDbModel.dateOfCreate,
        attachment = noteItemDbModel.attachment,
        isPinned = noteItemDbModel.isPined != 0,
        isFavorite = noteItemDbModel.isFavorite != 0,
        isArchive = noteItemDbModel.isArchive != 0,
        isDelete = noteItemDbModel.isDelete != 0,
        deletionDate = noteItemDbModel.deletionDate,
        categoryId = noteItemDbModel.categoryId,
    )

    fun mapListDbModelToListEntity(list: List<NoteItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

}

fun NoteItemMapper.mapBooleanToInt(value: Boolean): Int = if (value) 1 else 0