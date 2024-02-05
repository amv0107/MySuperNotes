package com.amv.simple.app.mysupernotes.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.amv.simple.app.mysupernotes.data.NoteItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.NoteItem

class MainListViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = NoteItemRepositoryImpl(application)
    private val getNoteListUseCase = GetNoteListUseCase(repository)

    val noteList: LiveData<List<NoteItem>> = getNoteListUseCase.getNoteList()


}