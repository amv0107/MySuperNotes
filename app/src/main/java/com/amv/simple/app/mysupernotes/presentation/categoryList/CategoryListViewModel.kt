package com.amv.simple.app.mysupernotes.presentation.categoryList

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
class CategoryListViewModel @Inject constructor(
    private val addCategoryItemUseCase: AddCategoryItemUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : ViewModel() {

    private val _categoryList = MutableLiveResult<List<CategoryItem>>()
    val categoryList: LiveResult<List<CategoryItem>> = _categoryList

    fun getCategoryList() = viewModelScope.launch {
        getCategoryListUseCase().collect{
            _categoryList.postValue(SuccessResult(it))
        }
    }

    fun addCategoryItem(idCategory: Int = 0, nameCategory: String, positionCategory: Int) = viewModelScope.launch {
        val item = CategoryItem(
            id = idCategory,
            position = positionCategory,
            name = parseText(nameCategory)
        )
        addCategoryItemUseCase(item)
    }

    private fun parseText(inputText: String?): String = inputText?.trim() ?: ""
}