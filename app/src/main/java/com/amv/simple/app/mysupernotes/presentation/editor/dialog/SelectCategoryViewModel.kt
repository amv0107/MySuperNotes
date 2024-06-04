package com.amv.simple.app.mysupernotes.presentation.editor.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.domain.category.AddCategoryItemUseCase
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryListUseCase
import com.amv.simple.app.mysupernotes.domain.util.SuccessResult
import com.amv.simple.app.mysupernotes.presentation.core.LiveResult
import com.amv.simple.app.mysupernotes.presentation.core.MutableLiveResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val addCategoryItemUseCase: AddCategoryItemUseCase
) : ViewModel() {

    private val _listCategory = MutableLiveResult<List<CategoryItem>>()
    val listCategory: LiveResult<List<CategoryItem>> = _listCategory

    fun getCategoryList() = viewModelScope.launch {
        getCategoryListUseCase().collect {
            _listCategory.postValue(SuccessResult(it))
        }
    }

    fun addCategoryItem(categoryName: String) = viewModelScope.launch {
        addCategoryItemUseCase(0, parseText(categoryName))
    }

    private fun parseText(inputText: String?): String = inputText?.trim() ?: ""
}