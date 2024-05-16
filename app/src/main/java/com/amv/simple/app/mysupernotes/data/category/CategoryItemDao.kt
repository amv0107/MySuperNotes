package com.amv.simple.app.mysupernotes.data.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryItemDao {

    @Query("SELECT * FROM category_items")
    fun getCategoryItemList(): Flow<List<CategoryDbModel>>

    @Insert
    suspend fun addCategoryItem(categoryDbModel: CategoryDbModel)

    @Update
    suspend fun updateCategoryItem(categoryDbModel: CategoryDbModel)

    @Delete
    suspend fun deleteCategoryItem(categoryDbModel: CategoryDbModel)
}