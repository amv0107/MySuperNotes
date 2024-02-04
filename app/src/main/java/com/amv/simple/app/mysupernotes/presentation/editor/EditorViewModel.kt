package com.amv.simple.app.mysupernotes.presentation.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.AddNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem
import kotlinx.coroutines.launch

class EditorViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = NoteItemRepositoryImpl(application)
    private val addNoteItemUseCase = AddNoteItemUseCase(repository)

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit> = _shouldCloseScreen

    fun addNoteItem(inputTitle: String?, inputTextContent: String?) {
        val title = parseText(inputTitle)
        val textContent = parseText(inputTextContent)
        val date = TimeManager.getCurrentTime()
        viewModelScope.launch {
            val item = NoteItem(
                title = title,
                textContent = textContent,
                date = date
            )
            addNoteItemUseCase(item)
            finishWork()
        }
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    private fun parseText(inputText: String?): String = inputText?.trim() ?: ""
}