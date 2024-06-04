package com.amv.simple.app.mysupernotes.domain.category

import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import com.amv.simple.app.mysupernotes.data.relations.CategoryAndNote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryAndNoteUseCase @Inject constructor(
    private val categoryItemRepository: CategoryItemRepositoryImpl
) {

    operator fun invoke(): Flow<List<CategoryAndNote>> {
        return categoryItemRepository.getCategoryAndNote()
    }
}