package com.amv.simple.app.mysupernotes.domain.category

import kotlinx.coroutines.flow.Flow

interface CategoryItemRepository {

    suspend fun addCategoryItem(categoryItem: CategoryItem)
    suspend fun editCategoryItem(categoryItem: CategoryItem)
    suspend fun deleteCategoryItem(categoryItem: CategoryItem)
    fun getCategoryItemById(categoryItemId: Int): Flow<CategoryItem>
    fun getCategoryItemList(): Flow<List<CategoryItem>>

}