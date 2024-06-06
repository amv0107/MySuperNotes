package com.amv.simple.app.mysupernotes.domain.note

import com.amv.simple.app.mysupernotes.data.note.NoteItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNoteListUseCase @Inject constructor(
    private val noteItemRepository: NoteItemRepositoryImpl
) {

    fun getNoteList(
        typeList: TypeList,
        categoryIdForFilter: Int?
    ): Flow<List<NoteItem>> {
        return noteItemRepository.getNoteItemList().map { list ->
            list
                .filter { item ->
                    when (typeList) {
                        TypeList.ARCHIVE_LIST -> {
                            item.isArchive && !item.isDelete
                        }

                        TypeList.FAVORITE_LIST -> {
                            item.isFavorite && !item.isDelete && !item.isArchive
                        }

                        TypeList.DELETE_LIST -> {
                            item.isDelete
                        }

                        TypeList.MAIN_LIST -> {
                            if (categoryIdForFilter != null)
                                !item.isDelete && !item.isArchive && item.categoryId == categoryIdForFilter
                            else
                                !item.isDelete && !item.isArchive
                        }
                    }
                }
                .sortedByDescending { it.isPinned }
        }
    }
}