package com.amv.simple.app.mysupernotes.presentation.mainList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import com.amv.simple.app.mysupernotes.domain.DeleteForeverNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.UpdateNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.util.ErrorResult
import com.amv.simple.app.mysupernotes.domain.util.NoteOrder
import com.amv.simple.app.mysupernotes.domain.util.OrderType
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.TypeList
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import com.amv.simple.app.mysupernotes.presentation.editor.TimeManager
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreStyleListNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _noteOrder = MutableLiveData<NoteOrder>()
    val noteOrder: LiveData<NoteOrder> = _noteOrder

    val formatDateTimeFlow = preferencesManager.formatDataTimeFlow
    val layoutManagerFlow = preferencesManager.layoutManagerFlow

    fun getNoteList(typeList: TypeList, noteOrder: NoteOrder = NoteOrder.DateCreate(OrderType.Ascending)) =
        viewModelScope.launch {
            val noteOrderFromPref = getNoteOrderForList(typeList)

            Log.d(
                "noteOrder",
                "noteOrder: ${noteOrderFromPref.javaClass.simpleName} orderType: ${noteOrderFromPref.orderType.javaClass.simpleName}"
            )

            getNoteListUseCase.getNoteList(typeList, noteOrderFromPref).collect { list ->
                Log.d(
                    "TAG",
                    "ViewModel: ${noteOrder.javaClass.simpleName}+${noteOrder.orderType.javaClass.simpleName}"
                )
                if (list.isEmpty())
                    _noteList.postValue(ErrorResult(NullPointerException()))
                else
                    _noteList.postValue(SuccessResult(list))
            }
        }

    fun getNoteOrder(typeList: TypeList) = viewModelScope.launch {
        _noteOrder.value = getNoteOrderForList(typeList)
    }

    private suspend fun getNoteOrderForList(typeList: TypeList): NoteOrder {
        var noteOrderFromPref: NoteOrder
        val job = viewModelScope.async {
            val noteOrderPref = preferencesManager.noteOrderFlow(typeList).first()
            val orderTypeFromPref = when (noteOrderPref.orderType) {
                "Descending" -> OrderType.Descending
                else -> OrderType.Ascending
            }

            noteOrderFromPref = when (noteOrderPref.noteOrder) {
                "DateCreate" -> NoteOrder.DateCreate(orderTypeFromPref)
                else -> NoteOrder.Title(orderTypeFromPref)
            }
            noteOrderFromPref
        }
        val noteOrder = job.await()
        _noteOrder.value = noteOrder
        return noteOrder
    }

    fun onTypeLayoutManager(dataStoreStyleListNotes: DataStoreStyleListNotes) = viewModelScope.launch {
        preferencesManager.updateTypeLayoutManager(dataStoreStyleListNotes)
    }

    fun changePin(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(noteItem.copy(isPinned = !noteItem.isPinned))
    }

    fun changeArchive(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(noteItem.copy(isArchive = false))
    }

    fun moveToTrash(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(noteItem.copy(isDelete = true, deleteDate = TimeManager.getCurrentTimeToDB()))
    }

    fun restoreDelete(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(noteItem.copy(isDelete = false, deleteDate = ""))
    }

    fun deleteForeverNoteItem(noteItem: NoteItem) = viewModelScope.launch {
        deleteForeverNoteItemUseCase(noteItem)
    }

    fun setNoteOrderForList(typeList: TypeList, noteOrder: NoteOrder) = viewModelScope.launch {
        preferencesManager.updateNoteOrder(typeList, noteOrder)
    }
}