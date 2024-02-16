package com.amv.simple.app.mysupernotes.domain

import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    fun getNoteList(
        typeList: TypeList
    ): Flow<List<NoteItem>> {
        return noteItemRepository.getNoteItemList().map { list ->
            list
                .filter { item ->
                    when (typeList) {
                        is TypeList.ArchiveList -> {
                            item.isArchive && !item.isDelete
                        }

                        is TypeList.FavoriteList -> {
                            item.isFavorite && !item.isDelete
                        }

                        is TypeList.DeleteList -> {
                            item.isDelete
                        }

                        is TypeList.MainList -> {
                            !item.isDelete && !item.isArchive
                        }
                    }
                }
                .sortedByDescending { it.isPinned }
        }
    }
}