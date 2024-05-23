package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import javax.inject.Inject

class DeleteCategoryItemUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    suspend operator fun invoke(categoryItem: CategoryItem) {
     // Сначало нужно все заметки помеченые данной категорией, пометить как без категории
     // хотя это возможно сделать средствами библиотеки room или средствами SQL
            categoryItemRepository.deleteCategoryItem(categoryItem)
    }
}