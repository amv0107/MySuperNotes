package com.amv.simple.app.mysupernotes.presentation.categoryList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.domain.category.AddCategoryItemUseCase
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val addCategoryItemUseCase: AddCategoryItemUseCase
) : ViewModel() {

    fun addCategoryItem(name: String, position: Int) = viewModelScope.launch {
        val item = CategoryItem(
            position = position,
            name = parseText(name)
        )
        addCategoryItemUseCase(item)
    }

    private fun parseText(inputText: String?): String = inputText?.trim() ?: ""
}