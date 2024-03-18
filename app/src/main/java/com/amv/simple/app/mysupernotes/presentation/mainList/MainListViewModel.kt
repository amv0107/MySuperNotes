package com.amv.simple.app.mysupernotes.presentation.mainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import com.amv.simple.app.mysupernotes.domain.DeleteForeverNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.UpdateNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import com.amv.simple.app.mysupernotes.presentation.editor.TimeManager
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreStyleListNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val getNoteListUseCase: GetNoteListUseCase,
    private val updateNoteItemUseCase: UpdateNoteItemUseCase,
    private val deleteForeverNoteItemUseCase: DeleteForeverNoteItemUseCase,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _noteList = MutableLiveResult<List<NoteItem>>(PendingResult())
    val noteList: LiveResult<List<NoteItem>> = _noteList

    val formatDateTimeFlow = preferencesManager.formatDataTimeFlow
    val layoutManagerFlow = preferencesManager.layoutManagerFlow


    fun getNoteList(typeList: TypeList) = viewModelScope.launch {
        getNoteListUseCase.getNoteList(typeList)
            .collect { list ->
                if (list.isEmpty())
                    _noteList.postValue(ErrorResult(NullPointerException()))
                else
                    _noteList.postValue(SuccessResult(list))
            }
    }

    fun onTypeLayoutManager(dataStoreStyleListNotes: DataStoreStyleListNotes) = viewModelScope.launch {
        preferencesManager.updateTypeLayoutManager(dataStoreStyleListNotes)
    }

    fun changePin(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isPinned = !noteItem.isPinned
            )
        )
    }

    fun changeArchive(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isArchive = false
            )
        )
    }

    fun moveToTrash(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isDelete = true,
                deleteDate = TimeManager.getCurrentTime()
            )
        )
    }

    fun restoreDelete(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isDelete = false,
                deleteDate = ""
            )
        )
    }

    fun deleteForeverNoteItem(noteItem: NoteItem) = viewModelScope.launch {
        deleteForeverNoteItemUseCase(noteItem)
    }

}