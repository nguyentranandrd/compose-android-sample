package com.capt.cleanarchitecturenoteapp.feature_note.di

import com.capt.cleanarchitecturenoteapp.feature_note.data.repository.NoteRepositoryImpl
import com.capt.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NoteRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindNoteRepository(noteRepository: NoteRepositoryImpl) : NoteRepository
}