package com.amv.simple.app.mysupernotes.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.amv.simple.app.mysupernotes.data.category.CategoryDbModel
import com.amv.simple.app.mysupernotes.data.note.NoteItemDbModel

data class CategoryAndNote(
    @Embedded val category: CategoryDbModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val notes: List<NoteItemDbModel>
)
