package com.amv.simple.app.mysupernotes.data

import com.amv.simple.app.mysupernotes.domain.NoteItem

class NoteItemMapper {

    fun mapEntityToDbModel(noteItem: NoteItem) = NoteItemDbModel(
        id = noteItem.id,
        title = noteItem.title,
        textContent = noteItem.textContent,
        date = noteItem.date,
        isPined = if (noteItem.isPinned) 1 else 0,
        isFavorite = if (noteItem.isFavorite) 1 else 0,
    )


    fun mapDbModelToEntity(noteItemDbModel: NoteItemDbModel) = NoteItem(
        id = noteItemDbModel.id,
        title = noteItemDbModel.title,
        textContent = noteItemDbModel.textContent,
        date = noteItemDbModel.date,
        isPinned = noteItemDbModel.isPined != 0,
        isFavorite = noteItemDbModel.isFavorite != 0,
    )

    fun mapListDbModelToListEntity(list: List<NoteItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}