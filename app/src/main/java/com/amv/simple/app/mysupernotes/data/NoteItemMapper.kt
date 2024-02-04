package com.amv.simple.app.mysupernotes.data

import com.amv.simple.app.mysupernotes.domain.NoteItem

class NoteItemMapper {

    fun mapEntityToDbModel(noteItem: NoteItem) = NoteItemDbModel(
        id = noteItem.id,
        title = noteItem.title,
        textContent = noteItem.textContent,
        date = noteItem.date
    )

    fun mapDbModelToEntity(noteItemDbModel: NoteItemDbModel) = NoteItem(
        id = noteItemDbModel.id,
        title = noteItemDbModel.title,
        textContent = noteItemDbModel.textContent,
        date = noteItemDbModel.date
    )

    fun mapListDbModelToListEntity(list: List<NoteItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}