package com.amv.simple.app.mysupernotes.di

import com.amv.simple.app.mysupernotes.data.AppDatabase
import com.amv.simple.app.mysupernotes.data.note.NoteItemDao
import com.amv.simple.app.mysupernotes.data.note.NoteItemMapper
import com.amv.simple.app.mysupernotes.data.note.NoteItemRepositoryImpl
import com.amv.simple.app.mysupernotes.domain.note.AddNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.note.DeleteForeverNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.note.GetNoteItemUseCase
import com.amv.simple.app.mysupernotes.domain.note.GetNoteListUseCase
import com.amv.simple.app.mysupernotes.domain.note.UpdateNoteItemUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteItemModule {
    @Provides
    @Singleton
    fun provideNoteItemDao(db: AppDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun provideNoteItemMapper() = NoteItemMapper()

    @Provides
    @Singleton
    fun provideGetNoteListUseCase(repository: NoteItemRepositoryImpl) = GetNoteListUseCase(repository)

    @Provides
    @Singleton
    fun provideAddNoteItemUseCase(repository: NoteItemRepositoryImpl) = AddNoteItemUseCase(repository)

    @Provides
    @Singleton
    fun provideGetNoteItemUseCase(repository: NoteItemRepositoryImpl) = GetNoteItemUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateNoteItemUseCase(repository: NoteItemRepositoryImpl) = UpdateNoteItemUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteForeverNoteItemUseCase(repository: NoteItemRepositoryImpl) =
        DeleteForeverNoteItemUseCase(repository)

    @Provides
    @Singleton
    fun provideNoteItemRepositoryImpl(dao: NoteItemDao, mapper: NoteItemMapper): NoteItemRepositoryImpl {
        return NoteItemRepositoryImpl(dao, mapper)
    }
}