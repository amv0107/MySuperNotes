package com.amv.simple.app.mysupernotes.di

import com.amv.simple.app.mysupernotes.data.AppDatabase
import com.amv.simple.app.mysupernotes.data.category.CategoryItemDao
import com.amv.simple.app.mysupernotes.data.category.CategoryItemMapper
import com.amv.simple.app.mysupernotes.data.category.CategoryItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.category.AddCategoryItemUseCase
import com.amv.simple.app.mysupernotes.domain.category.DeleteCategoryItemUseCase
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryItemByIdUseCase
import com.amv.simple.app.mysupernotes.domain.category.GetCategoryListUseCase
import com.amv.simple.app.mysupernotes.domain.category.UpdateCategoryItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CategoryItemModule {

    @Provides
    @Singleton
    fun provideCategoryItemDao(db: AppDatabase) = db.categoryDao()

    @Provides
    @Singleton
    fun provideCategoryItemMapper() = CategoryItemMapper()

    @Provides
    @Singleton
    fun provideAddCategoryItemUseCase(repository: CategoryItemRepositoryImpl) = AddCategoryItemUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCategoryItemUseCase(repository: CategoryItemRepositoryImpl) = DeleteCategoryItemUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoryItemByIdUseCase(repository: CategoryItemRepositoryImpl) = GetCategoryItemByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCategoryListUseCase(repository: CategoryItemRepositoryImpl) = GetCategoryListUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCategoryItemUseCase(repository: CategoryItemRepositoryImpl) = UpdateCategoryItemUseCase(repository)

    @Provides
    @Singleton
    fun provideCategoryItemRepository(dao: CategoryItemDao, mapper: CategoryItemMapper): CategoryItemRepositoryImpl {
        return CategoryItemRepositoryImpl(dao, mapper)
    }
}