package com.amv.simple.app.mysupernotes.presentation.editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import com.amv.simple.app.mysupernotes.domain.AddNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.GetNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem
import com.amv.simple.app.mysupernotes.domain.UpdateNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val addNoteItemUseCase: AddNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
    private val updateNoteItemUseCase: UpdateNoteItemUseCase,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _noteItem = MutableLiveResult<NoteItem>(PendingResult())
    val noteItem: LiveResult<NoteItem>
        get() = _noteItem

    val formatDataTimeFlow = preferencesManager.formatDataTimeFlow

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    fun addNoteItem(inputTitle: String?, inputTextContent: String?) = viewModelScope.launch {
        val item = NoteItem(
            title = parseText(inputTitle),
            textContent = parseText(inputTextContent),
            date = TimeManager.getCurrentTimeToDB()
        )
        addNoteItemUseCase(item)
        finishWork()
    }

    fun restoreDelete(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isDelete = false,
                deleteDate = ""
            )
        )
        finishWork()
    }

    fun getNoteItem(noteItemId: Int) = viewModelScope.launch {
        val item = getNoteItemUseCase(noteItemId)
        item.collect {
            _noteItem.postValue(SuccessResult(it))
        }
    }

    fun updateNoteItem(inputTitle: String?, inputTextContent: String?) = viewModelScope.launch {
        // TODO: Не уверен что правильно использовал !! ведь у нас там может быть пусто
        val item = _noteItem.value.takeSuccess()?.copy(
            title = parseText(inputTitle),
            textContent = parseText(inputTextContent)
        )!!
        updateNoteItemUseCase(item)
        finishWork()
    }

    fun changePin() = viewModelScope.launch {
        val item = _noteItem.value.takeSuccess()?.let {
            it.copy(isPinned = !it.isPinned)
        }
        updateNoteItemUseCase(item!!)
    }

    fun changeFavorite() = viewModelScope.launch {
        val item = _noteItem.value.takeSuccess()?.let {
            it.copy(isFavorite = !it.isFavorite)
        }
        updateNoteItemUseCase(item!!)
    }

    fun moveNoteToTrash() = viewModelScope.launch {
        val item = _noteItem.value.takeSuccess()?.let {
            it.copy(
                isDelete = true,
                deleteDate = TimeManager.getCurrentTimeToDB()
            )
        }
        updateNoteItemUseCase(item!!)
        finishWork()
    }

    fun changeArchive() = viewModelScope.launch {
        val item = _noteItem.value.takeSuccess()?.let {
            it.copy(isArchive = !it.isArchive)
        }
        updateNoteItemUseCase(item!!)
        finishWork()
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun parseText(inputText: String?): String = inputText?.trim() ?: ""
}