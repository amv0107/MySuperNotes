package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddCategoryItemUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    suspend operator fun invoke(categoryId: Int, categoryName: String) {
        if (categoryName.isEmpty())
            return

        val currentListOfCategory = categoryItemRepository.getCategoryItemList().first()

        val position = if (categoryId == 0)
            currentListOfCategory.maxOf { it.position } + 1
        else
            currentListOfCategory.first { it.id == categoryId }.position

        val categoryItem = CategoryItem(
            categoryId,
            position,
            categoryName,
        )
        categoryItemRepository.addCategoryItem(categoryItem)
    }
}