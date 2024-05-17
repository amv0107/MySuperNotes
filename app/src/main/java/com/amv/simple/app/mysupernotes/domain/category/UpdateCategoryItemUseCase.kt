package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import javax.inject.Inject

class UpdateCategoryItemUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    suspend operator fun invoke(categoryItem: CategoryItem) =
        categoryItemRepository.editCategoryItem(categoryItem)
}