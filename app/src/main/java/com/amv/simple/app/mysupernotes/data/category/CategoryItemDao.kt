package com.amv.simple.app.mysupernotes.data.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryItemDao {

    @Query("SELECT * FROM category_items ORDER BY position ASC")
    fun getCategoryItemList(): Flow<List<CategoryDbModel>>

    @Query("SELECT * FROM category_items WHERE id = :categoryItemId")
    fun getCategoryItem(categoryItemId: Int): Flow<CategoryDbModel>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategoryItem(categoryDbModel: CategoryDbModel)

    @Delete
    suspend fun deleteCategoryItem(categoryDbModel: CategoryDbModel)
}