package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.relations.CategoryAndNote
import kotlinx.coroutines.flow.Flow

interface CategoryItemRepository {

    suspend fun addCategoryItem(categoryItem: CategoryItem)
    suspend fun deleteCategoryItem(categoryItem: CategoryItem)
    suspend fun getCategoryItemById(categoryItemId: Int): CategoryItem
    fun getCategoryItemList(): Flow<List<CategoryItem>>

    fun getCategoryAndNote(): Flow<List<CategoryAndNote>>

}