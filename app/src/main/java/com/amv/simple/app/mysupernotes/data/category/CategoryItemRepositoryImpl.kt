package com.amv.simple.app.mysupernotes.data.category

import com.amv.simple.app.mysupernotes.data.relations.CategoryAndNote
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import com.amv.simple.app.mysupernotes.domain.category.CategoryItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryItemRepositoryImpl @Inject constructor(
    private val dao: CategoryItemDao,
    private val mapper: CategoryItemMapper
): CategoryItemRepository {
    override suspend fun addCategoryItem(categoryItem: CategoryItem) =
        dao.addCategoryItem(mapper.mapEntityToDbModel(categoryItem))

    override suspend fun deleteCategoryItem(categoryItem: CategoryItem) =
        dao.deleteCategoryItem(mapper.mapEntityToDbModel(categoryItem))

    override suspend fun getCategoryItemById(categoryItemId: Int): CategoryItem =
       mapper.mapDbModelToEntity(dao.getCategoryItem(categoryItemId))

    override fun getCategoryItemList(): Flow<List<CategoryItem>> =
        dao.getCategoryItemList().map {categoryDbModelList ->
            mapper.mapListDbModelToListEntity(categoryDbModelList)
        }

    override fun getCategoryAndNote(): Flow<List<CategoryAndNote>> =
        dao.getCategoryAndNote()
}