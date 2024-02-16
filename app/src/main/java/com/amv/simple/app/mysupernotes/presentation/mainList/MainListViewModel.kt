package com.amv.simple.app.mysupernotes.presentation.mainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.domain.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.TypeList
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

    fun getNoteList(typeList: TypeList) = viewModelScope.launch {
        getNoteListUseCase.getNoteList(typeList)
            .collect { list ->
                if (list.isEmpty())
                    _noteList.postValue(ErrorResult(NullPointerException()))
                else
                    _noteList.postValue(SuccessResult(list))


            }
    }

}