package com.amv.simple.app.mysupernotes.presentation.mainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.domain.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListViewModel @Inject constructor(
    private val getNoteListUseCase: GetNoteListUseCase
) : ViewModel() {

    private val _noteList = MutableLiveResult<List<NoteItem>>(PendingResult())
    val noteList: LiveResult<List<NoteItem>> = _noteList

    fun getNoteList(
        archiveNotes: Boolean = false,
        favoriteNotes: Boolean = false,
        deleteNotes: Boolean = false
    ) = viewModelScope.launch {
        getNoteListUseCase.getNoteList()
            .collect { list ->
                if (archiveNotes)
                    _noteList.postValue(SuccessResult(list.filter { item -> item.isArchive && !item.isDelete }))
                if (favoriteNotes)
                    _noteList.postValue(SuccessResult(list.filter { item -> item.isFavorite && !item.isDelete }))
                if (deleteNotes)
                    _noteList.postValue(SuccessResult(list.filter { item -> item.isDelete }))
                if (!archiveNotes && !favoriteNotes && !deleteNotes)
                    _noteList.postValue(SuccessResult(list.filter { item -> !item.isDelete && !item.isArchive }))
            }
    }

}