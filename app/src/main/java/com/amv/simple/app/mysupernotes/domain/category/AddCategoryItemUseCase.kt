package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import javax.inject.Inject

class AddCategoryItemUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    suspend operator fun invoke(categoryItem: CategoryItem) {
        if (categoryItem.name.isNotEmpty())
            categoryItemRepository.addCategoryItem(categoryItem)
    }
}