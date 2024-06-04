package com.amv.simple.app.mysupernotes.data.category

import com.amv.simple.app.mysupernotes.domain.category.CategoryItem

class CategoryItemMapper {

    fun mapEntityToDbModel(categoryItem: CategoryItem) = CategoryDbModel(
        id = categoryItem.id,
        position = categoryItem.position,
        name = categoryItem.name,
    )

    fun mapDbModelToEntity(categoryDbModel: CategoryDbModel) = CategoryItem(
        id = categoryDbModel.id,
        position = categoryDbModel.position,
        name = categoryDbModel.name,
    )

    fun mapListDbModelToListEntity(list: List<CategoryDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}