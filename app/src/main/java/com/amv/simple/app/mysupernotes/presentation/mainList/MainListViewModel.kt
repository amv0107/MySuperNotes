package com.amv.simple.app.mysupernotes.presentation.mainList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryListUseCase
import com.amv.simple.app.mysupernotes.domain.note.DeleteForeverNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.note.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.note.GetNotesByCategoryUseCase
import com.amv.simple.app.mysupernotes.domain.note.NoteItem
import com.amv.simple.app.mysupernotes.domain.note.UpdateNoteItemUseCase
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
    private val getNotesByCategoryUseCase: GetNotesByCategoryUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val updateNoteItemUseCase: UpdateNoteItemUseCase,
    private val deleteForeverNoteItemUseCase: DeleteForeverNoteItemUseCase,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _noteList = MutableLiveResult<List<NoteItem>>(PendingResult())
    val noteList: LiveResult<List<NoteItem>> = _noteList

    private val _categoryList = MutableLiveData<List<CategoryItem>>()
    val categoryList: LiveData<List<CategoryItem>> = _categoryList

    private val _filterByCategoryId = MutableLiveData<Int?>(null)
    val filterByCategoryId: LiveData<Int?> = _filterByCategoryId

    val formatDateTimeFlow = preferencesManager.formatDataTimeFlow
    val layoutManagerFlow = preferencesManager.layoutManagerFlow

    fun setFilterByCategoryId(id: Int?) {
        _filterByCategoryId.value = id
    }
    fun getCategoryList() = viewModelScope.launch {
        getCategoryListUseCase().collect{ categoryItemList ->
            val list: MutableList<CategoryItem> = categoryItemList as MutableList
            list.removeIf { category ->
                category.id == 1
            }
            _categoryList.postValue(list)
        }
    }

    fun getNoteList(typeList: TypeList) = viewModelScope.launch {
        getNoteListUseCase.getNoteList(typeList, _filterByCategoryId.value)
            .collect { list ->
                if (list.isEmpty())
                    _noteList.postValue(ErrorResult(NullPointerException()))
                else
                    _noteList.postValue(SuccessResult(list))
            }
    }

    fun getNotesByCategory(categoryId: Int) = viewModelScope.launch {
        getNotesByCategoryUseCase(categoryId).collect { list ->
            if (list.isEmpty())
                _noteList.postValue(ErrorResult(NullPointerException()))
            else
                _noteList.postValue(SuccessResult(list))
        }
    }

    fun onTypeLayoutManager(dataStoreStyleListNotes: DataStoreStyleListNotes) =
        viewModelScope.launch {
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
                deletionDate = TimeManager.getCurrentTimeToDB()
            )
        )
    }

    fun restoreDelete(noteItem: NoteItem) = viewModelScope.launch {
        updateNoteItemUseCase(
            noteItem.copy(
                isDelete = false,
                deletionDate = 0
            )
        )
    }

    fun deleteForeverNoteItem(noteItem: NoteItem) = viewModelScope.launch {
        deleteForeverNoteItemUseCase(noteItem)
    }

}