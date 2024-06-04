package com.amv.simple.app.mysupernotes.presentation.categoryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.relations.CategoryAndNote
import com.amv.simple.app.mysupernotes.domain.category.AddCategoryItemUseCase
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import com.amv.simple.app.mysupernotes.domain.category.DeleteCategoryItemUseCase
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryAndNoteUseCase
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryListUseCase
import com.amv.simple.app.mysupernotes.domain.note.GetNotesByCategoryUseCase
import com.amv.simple.app.mysupernotes.domain.note.UpdateNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val addCategoryItemUseCase: AddCategoryItemUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val deleteCategoryItemUseCase: DeleteCategoryItemUseCase,
    private val getNotesByCategoryUseCase: GetNotesByCategoryUseCase,
    private val updateNoteItemUseCase: UpdateNoteItemUseCase,
    private val getCategoryAndNoteUseCase: GetCategoryAndNoteUseCase
) : ViewModel() {

    private val _categoryList = MutableLiveResult<List<CategoryItem>>()
    val categoryList: LiveResult<List<CategoryItem>> = _categoryList

    private val _categoryAndNotes = MutableLiveResult<List<CategoryAndNote>>()
    val categoryAndNote: LiveResult<List<CategoryAndNote>> = _categoryAndNotes

    fun getCategoryList() = viewModelScope.launch {
        getCategoryListUseCase().collect {
            _categoryList.postValue(SuccessResult(it))
        }
    }

    fun getCategoryAndNote() = viewModelScope.launch {
        getCategoryAndNoteUseCase().collect{
            _categoryAndNotes.postValue(SuccessResult(it))
        }
    }

    fun addCategoryItem(categoryId: Int = 0, categoryName: String) = viewModelScope.launch {
        addCategoryItemUseCase(categoryId, parseText(categoryName))
    }

    fun deleteCategoryItem(categoryItem: CategoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Мне кажется что тут неправильная работа с корутинами,
            //  мне нужно стачало изменить все заметки (т.е. присвоить им категорию 0)
            //  и только после этого удалять категорию
            //  плюс ко всему я пока не понимаю как этот список к нам поступает,
            //  успевает ли корутина обновить эти заметки, не возникает ли каких либо возможных
            //  проблем с flow
            getNotesByCategoryUseCase(categoryItem.id).collect { listNotesByCategory ->
                listNotesByCategory.forEach { note ->
                    val updateNote = note.copy(categoryId = 1)
                    updateNoteItemUseCase.invoke(updateNote)
                }
                deleteCategoryItemUseCase(categoryItem)
            }
        }
    }

    private fun parseText(inputText: String?): String = inputText?.trim() ?: ""
}