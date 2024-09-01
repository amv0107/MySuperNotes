package com.amv.simple.app.mysupernotes.presentation.editor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import com.amv.simple.app.mysupernotes.data.note.Attachment
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryItemByIdUseCase
import com.amv.simple.app.mysupernotes.domain.note.AddNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.note.GetNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.note.NoteItem
import com.amv.simple.app.mysupernotes.domain.note.UpdateNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.util.PendingResult
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.domain.util.isEmpty
import com.amv.simple.app.mysupernotes.domain.util.takeSuccess
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import com.amv.simple.app.mysupernotes.presentation.core.parseText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias CategoryName = String

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val addNoteItemUseCase: AddNoteItemUseCase,
    private val getNoteItemUseCase: GetNoteItemUseCase,
    private val updateNoteItemUseCase: UpdateNoteItemUseCase,
    private val getCategoryItemByIdUseCase: GetCategoryItemByIdUseCase,
    preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _noteItem = MutableLiveResult<NoteItem>(PendingResult())
    val noteItem: LiveResult<NoteItem> = _noteItem

    private val _categoryItemName = MutableLiveData<String>()
    val categoryItemName: LiveData<String> = _categoryItemName

    private val _categoryItemId = MutableLiveData<Int>(1)
    val categoryItemId: LiveData<Int> = _categoryItemId

    val formatDataTimeFlow = preferencesManager.formatDataTimeFlow

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    fun addNoteItem(inputTitle: String?, inputTextContent: String?) = viewModelScope.launch {
        val item = NoteItem(
            title = parseText(inputTitle),
            textContent = parseText(inputTextContent),
            dateOfCreate = TimeManager.getCurrentTimeToDB(),
            categoryId = _categoryItemId.value!!, // TODO: !!!
        )
        addNoteItemUseCase(item)
        finishWork()
    }

    fun insertAttachment(
        type: Attachment.Type,
        path: String,
        description: String,
        fileName: String
    ) {
        val attachment = Attachment(type, path, description, fileName)
        viewModelScope.launch(Dispatchers.IO) {
            if (_noteItem.value.isEmpty()){
                Log.d("TAG", "insertAttachment: SaveNoteItem and attachment audioRecord")
            } else {
                updateNoteItemUseCase(
                    _noteItem.value.takeSuccess()!!.copy(
                        attachment = _noteItem.value.takeSuccess()!!.attachment + attachment
                    )
                )
            }
        }

    }

    fun restoreDelete(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isDelete = false,
                deletionDate = 0
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
            textContent = parseText(inputTextContent),
            categoryId = _categoryItemId.value!! // TODO: !!!
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
                deletionDate = TimeManager.getCurrentTimeToDB()
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

    fun getCategoryNameById(categoryItemId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _categoryItemName.postValue(getCategoryItemByIdUseCase.invoke(categoryItemId).name)
        _categoryItemId.postValue(categoryItemId)
    }

}