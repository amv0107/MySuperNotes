package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import javax.inject.Inject

class GetCategoryItemByIdUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    suspend operator fun invoke(categoryItemId: Int): CategoryItem {
        // TODO: String Resource 
        return if (categoryItemId == 0)
            CategoryItem(0, 0, "WithoutAAAA")
        else
            categoryItemRepository.getCategoryItemById(categoryItemId)
    }

}