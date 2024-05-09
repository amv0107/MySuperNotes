package com.amv.simple.app.mysupernotes.domain

import android.util.Log
import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.util.NoteOrder
import com.amv.simple.app.mysupernotes.domain.util.OrderType
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    fun getNoteList(
        typeList: TypeList,
        noteOrder: NoteOrder,
    ): Flow<List<NoteItem>> {
        return noteItemRepository.getNoteItemList().map { notes ->
            Log.d("TAG", "UseCase: ${noteOrder.javaClass.simpleName}+${noteOrder.orderType.javaClass.simpleName}")
            notes.filter { item ->
                when (typeList) {
                    TypeList.ARCHIVE_LIST -> item.isArchive && !item.isDelete
                    TypeList.FAVORITE_LIST -> item.isFavorite && !item.isDelete && !item.isArchive
                    TypeList.DELETE_LIST -> item.isDelete
                    TypeList.MAIN_LIST -> !item.isDelete && !item.isArchive
                }
            }

            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title }
                        is NoteOrder.DateCreate -> notes.sortedBy { it.date }
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title }
                        is NoteOrder.DateCreate -> notes.sortedByDescending { it.date }
                    }
                }
            }.sortedByDescending { it.isPinned }

        }
    }
}