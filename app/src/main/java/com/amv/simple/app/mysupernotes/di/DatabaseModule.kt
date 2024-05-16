package com.amv.simple.app.mysupernotes.di

import android.content.Context
import androidx.room.Room
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DB_NAME
    )
        .build()

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase) = db.noteDao()

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
    fun provideDeleteForeverNoteItemUseCase(repository: NoteItemRepositoryImpl) = DeleteForeverNoteItemUseCase(repository)

    @Provides
    @Singleton
    fun provideMapper() = NoteItemMapper()

    @Provides
    @Singleton
    fun provideRepositoryImpl(dao: NoteItemDao, mapper: NoteItemMapper): NoteItemRepositoryImpl {
        return NoteItemRepositoryImpl(dao, mapper)
    }
}