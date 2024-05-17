package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryItemByIdUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    operator fun invoke(categoryItemId: Int): Flow<CategoryItem> =
        categoryItemRepository.getCategoryItemById(categoryItemId)
}